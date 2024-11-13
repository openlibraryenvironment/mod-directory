package org.olf.reshare

import grails.gorm.multitenancy.Tenants;
import grails.util.Holders;
import groovy.util.logging.Slf4j;
import org.olf.okapi.modules.directory.DirectoryEntry;
import org.grails.datastore.mapping.engine.event.PostDeleteEvent
import org.grails.datastore.mapping.engine.event.PostInsertEvent
import org.grails.datastore.mapping.engine.event.PreInsertEvent
import org.grails.datastore.mapping.engine.event.PostUpdateEvent
import org.grails.datastore.mapping.engine.event.SaveOrUpdateEvent
import org.grails.datastore.mapping.engine.event.AbstractPersistenceEvent
import javax.annotation.PostConstruct;
import groovy.transform.CompileStatic
import org.springframework.context.ApplicationListener
import org.springframework.context.ApplicationEvent
import org.grails.orm.hibernate.AbstractHibernateDatastore
import grails.gorm.transactions.Transactional
import static grails.async.Promises.*
import grails.async.Promise
import java.text.SimpleDateFormat



/**
 * This class listens for asynchronous domain class events and fires of any needed indications
 * This is the grails async framework in action - the notifications are in a separate thread to
 * the actual save or update of the domain class instance. Handlers should be short lived and if
 * work is needed, spawn a worker task.
 */
public class AppListenerService implements ApplicationListener {

  EventPublicationService eventPublicationService

  def republish(String tenant) {
    log.debug("Republish(${tenant})");
    DirectoryEntry.list().each { de ->
      logDirectoryEvent(de, tenant+'_mod_directory');
    }
  }

  /**
   * It's not really enough to do this afterInsert - we actually want this event to fire after the transaction
   * has committed. Be aware that the event may arrive back before the transaction has committed.
   */
  void afterInsert(PostInsertEvent event) {
    if ( event.entityObject instanceof DirectoryEntry ) {
      DirectoryEntry de = (DirectoryEntry)event.entityObject;
      log.debug("DirectoryEntry inserted ${de}");
      logDirectoryEvent(de, Tenants.currentId(event.source));
    }
  }

  // https://www.codota.com/code/java/methods/org.hibernate.event.spi.PostUpdateEvent/getPersister
  void afterUpdate(PostUpdateEvent event) {
    if ( event.entityObject instanceof DirectoryEntry ) {
      DirectoryEntry de = (DirectoryEntry)event.entityObject;
      log.debug("Directory entry updated ${de}");
      logDirectoryEvent(de, Tenants.currentId(event.source));
    }
  }

  void afterDelete(PostDeleteEvent event) {
    if ( event.entityObject instanceof DirectoryEntry ) {
      DirectoryEntry de = (DirectoryEntry)event.entityObject
      log.debug("Directory entry deleted ${de}")
      logDirectoryEvent(de, Tenants.currentId(event.source), true)
    }
  }

  public void onApplicationEvent(org.springframework.context.ApplicationEvent event){
    // log.debug("--> ${event?.class.name} ${event}");
    if ( event instanceof AbstractPersistenceEvent ) {
      if ( event instanceof PostUpdateEvent ) {
        afterUpdate(event);
      }
      else if ( event instanceof PreInsertEvent ) {
        // beforeInsert(event);
      }
      else if ( event instanceof PostInsertEvent ) {
        afterInsert(event);
      }
      else if ( event instanceof SaveOrUpdateEvent ) {
        // On save the record will not have an ID, but it appears that a subsequent event gets triggered
        // once the id has been allocated
      }
      else if ( event instanceof PostDeleteEvent ) {
        afterDelete(event)
      }
      else {
        // log.debug("No special handling for appliaction event of class ${event}");
      }
    }
    else {
      // log.debug("Event is not a persistence event: ${event}");
    }
  }


  /**
   *  it's important that tenant is of the form X_mod_directory and not just X
   */
  private logDirectoryEvent(DirectoryEntry de, String tenant, boolean deleted = false) {

    log.debug("logDirectoryEvent(id:${de.id} version:${de.version} / ${tenant})");

    String topic = "${tenant}_DirectoryEntryUpdate".toString()
    Map entry_data

    if (deleted) {
      entry_data = [:]
      entry_data.slug = de.slug
      entry_data.id = de.id
      entry_data.deleted = true
    } else {
      entry_data = makeDirentJSON(de, false, true, false, false)
    }

    log.debug("Publish DirectoryEntryChange_ind event on topic ${topic} ${entry_data.slug}");

    Promise p = task {
      eventPublicationService.publishAsJSON(
          topic,
          null,             // key
          [
            event:'DirectoryEntryChange_ind',
            tenant: tenant.replaceAll('_mod_directory',''),  // Strip out the module name so we're left with just the tenant
            oid:'org.olf.okapi.modules.directory.DirectoryEntry:'+de.id,
            payload:entry_data
          ]
      );
    }

    p.onError { Throwable err ->
      log.error("There was a problem publishing the directory event",err);
    }

    log.debug("logDirectoryEvent(id:${de.id} version:${de.version} / ${tenant}) -- COMPLETE");
  }

  private Map getCustprops(com.k_int.web.toolkit.custprops.types.CustomPropertyContainer svc, boolean include_private_custprops=false) {
    Map result = [:]
    svc.value.each { cp ->
      // If we have not already mapped a value for this key, create an array in the response
      if ( ( include_private_custprops == true ) ||
           ( ( include_private_custprops == false ) && ( cp.definition.defaultInternal == false ) ) ) {
        if ( result[cp.definition.name] == null ) {
          result[cp.definition.name] = [ getCPValue(cp.value) ]
        }
        else {
          // otherwise, add this value to the existing array
          result[cp.definition.name].add(getCPValue(cp.value))
        }   
      }
    }
    log.debug("Adding service account custom properties: ${result}");
    return result;
  }

  private String getCPValue(Object o) {
    String result = null;
    if ( o != null ) {
      if ( o instanceof com.k_int.web.toolkit.refdata.RefdataValue ) {
        result = ((com.k_int.web.toolkit.refdata.RefdataValue)o).value
      }
      else {
        result = o.toString();
      }
    }
    return result;
  }

  public Map makeDirentJSON(DirectoryEntry de, 
                            boolean include_units=false, 
                            boolean include_private_custprops=false,
                            boolean use_public_profile=false,
                            boolean add_addresses=false) {

    String last_modified_str = null;
    if ( ( de.pubLastUpdate != null ) && ( de.pubLastUpdate > 0 ) ) {
      Date d = new Date(de.pubLastUpdate)
      def sdf = new SimpleDateFormat('yyyy-MM-dd\'T\'HH:mm:ssX')
      last_modified_str = sdf.format(d)
    }

    Map entry_data = [
      id: de.id,  // We are using assigned identifiers now!
      lastModified: last_modified_str,
      name: de.name,
      slug: de.slug,
      foafUrl: de.foafUrl,
      brandingUrl: de.brandingUrl,
      services:[],
      symbols:[],
      description: de.description,
      entryUrl: de.entryUrl,
      phoneNumber: de.phoneNumber,
      emailAddress: de.emailAddress,
      contactName: de.contactName,
      lmsLocationCode: de.lmsLocationCode,
      tags: de.tags?.collect {it?.value},
      type: de.type?.value,
      customProperties: getCustprops(de.customProperties, include_private_custprops),
      members:[],
      addresses: [],
    ]

    if ( use_public_profile ) {
      // We omit several properties for the public interface
    }
    else {
      entry_data.status = de.status?.value;
    }

    de.services.each { svc ->
      entry_data.services.add([
                    slug:svc.slug, 
                    service:[
                      name: svc.service.name,
                      address: svc.service.address,
                      type: svc.service.type?.value,
                      businessFunction: svc.service.businessFunction?.value,
                    ],
                    customProperties: getCustprops(svc.customProperties, include_private_custprops)])
    }

    de.symbols.each { sym ->
      entry_data.symbols.add (
        [
          authority: sym.authority.symbol,
          symbol: sym.symbol,
          priority: sym.priority
        ]
      );
    }

    de.members.each { mem ->
      if ( mem?.memberOrg?.slug ) {
        entry_data.members.add(['memberOrg':mem?.memberOrg?.slug])
      }
    }


    if ( de.parent != null ) {
      entry_data.parent = [
        id: de.parent.id,
        slug: de.parent.slug,
        name: de.parent.name
      ]
    }

    if ( include_units ) {
      entry_data.units = []
      de.units.each { unit ->
        entry_data.units.add(makeDirentJSON(unit, true, include_private_custprops, use_public_profile, add_addresses))
      }
    }

    if (add_addresses) {
      de.addresses.each { address ->
        def transformedAddress = [
          id: address.id,
          addressLabel: address.addressLabel,
          countryCode: address.countryCode,
          tags: address.tags.collect { tag ->
            [
              normValue: tag.normValue,
              value: tag.value
            ]
          },
          owner: address.owner?.id,
          lines: []
        ]

        address.lines.each { line ->
          transformedAddress.lines.add([
            id: line.id,
            seq: line.seq,
            value: line.value,
            type: line.type.value,
            owner: line.owner.id
          ])
        }

        entry_data.addresses.add(transformedAddress)
      }
    }

    return entry_data
  }
}


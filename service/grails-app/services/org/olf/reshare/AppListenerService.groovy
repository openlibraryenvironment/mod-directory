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


/**
 * This class listens for asynchronous domain class events and fires of any needed indications
 * This is the grails async framework in action - the notifications are in a separate thread to
 * the actual save or update of the domain class instance. Handlers should be short lived and if
 * work is needed, spawn a worker task.
 */
public class AppListenerService implements ApplicationListener {

  EventPublicationService eventPublicationService

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
      else {
        // log.debug("No special handling for appliaction event of class ${event}");
      }
    }
    else {
      // log.debug("Event is not a persistence event: ${event}");
    }
  }


  private logDirectoryEvent(DirectoryEntry de, String tenant) {

    String topic = "${tenant}_DirectoryServiceEvents".toString()

    Map entry_data = [
      id: de.id,
      name: de.name,
      slug: de.slug,
      foafUrl: de.foafUrl
    ]

    log.debug("Publish NewPatronRequest_ind event on topic ${topic} ${entry_data}");

    if ( de.parent != null ) {
      entry_data.parent = [
        id: de.parent.id,
        slug: de.parent.slug
      ]
    }

    eventPublicationService.publishAsJSON(
          topic,
          null,             // key
          [
            event:'DirectoryEntryChange_ind',
            tenant: tenant,
            oid:'org.olf.okapi.modules.directory.DirectoryEntry:'+de.id,
            payload:entry_data
          ]
          );
  }
}


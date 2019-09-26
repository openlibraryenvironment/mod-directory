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
    }
  }

 // https://www.codota.com/code/java/methods/org.hibernate.event.spi.PostUpdateEvent/getPersister
  void afterUpdate(PostUpdateEvent event) {
    if ( event.entityObject instanceof PatronRequest ) {
      DirectoryEntry de = (DirectoryEntry)event.entityObject;
      log.debug("Directory entry updated ${de}");
    }
  }
}


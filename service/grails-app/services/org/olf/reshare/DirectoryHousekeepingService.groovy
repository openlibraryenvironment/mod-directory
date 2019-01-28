package org.olf.reshare



import grails.gorm.multitenancy.Tenants
import grails.events.annotation.Subscriber
import grails.gorm.multitenancy.WithoutTenant
import grails.gorm.transactions.Transactional

import org.olf.okapi.modules.directory.Service
import com.k_int.web.toolkit.refdata.RefdataValue
import com.k_int.web.toolkit.custprops.CustomPropertyDefinition
import com.k_int.web.toolkit.custprops.types.CustomPropertyText;

import grails.databinding.SimpleMapDataBindingSource 

/**
 * This service works at the module level, it's often called without a tenant context.
 */
@Transactional
class DirectoryHousekeepingService {

  def grailsWebDataBinder

  /**
   * This is called by the eventing mechanism - There is no web request context
   * this method is called after the schema for a tenant is updated.
   */
  @Subscriber('okapi:schema_update')
  public void onSchemaUpdate(tn, tid) {
    log.debug("DirectoryHousekeepingService::onSchemaUpdate(${tn},${tid})")
    try {
      // This is a workaround until we can implement an event that fires after all refdata properties
      // are configured
      Thread.sleep(2000);
    }
    catch ( Exception e ) {
    }
    setupData(tn, tid);
  }

  /**
   * Put calls to estabish any required reference data in here. This method MUST be communtative - IE repeated calls must leave the 
   * system in the same state. It will be called regularly throughout the lifecycle of a project. It is common to see calls to
   * lookupOrCreate, or "upsert" type functions in here."
   */
  private void setupData(tenantName, tenantId) {

    log.info("DirectoryHousekeepingService::setupData(${tenantName},${tenantId})");

    // Establish a database session in the context of the activated tenant. You can use GORM domain classes inside the closure
    // Please note that there is custom databinding at play here which means that in some places what appear to be strings
    // are being converted into java objects looked up in the database. 
    Tenants.withId(tenantId) {
      def cp_url = ensureTextProperty('url');
      def iso_18626_loopback_service = ensureService('loopback-iso-18626', 'ISO18626', [ 'url': 'http://localhost:9130/rs/iso18626' ]);
    }

    log.info("DirectoryHousekeepingService::setupData(${tenantName},${tenantId}) Completed Normally");
  }

  private CustomPropertyDefinition ensureTextProperty(String name) {
    CustomPropertyDefinition result = CustomPropertyDefinition.findByName(name) ?: new CustomPropertyDefinition(
                                        name:name,
                                        type:com.k_int.web.toolkit.custprops.types.CustomPropertyText.class
                                      ).save(flush:true, failOnError:true);
    return result;
  }

  private Service ensureService(String name, String type, Map custProps) {
    // def loopback_iso_service = new org.olf.okapi.modules.directory.Service()
    // Map service_props = [
    // ]
    // grailsWebDataBinder.bind loopback_iso_service, service_props as SimpleMapDataBindingSource
    return null;
  }
}


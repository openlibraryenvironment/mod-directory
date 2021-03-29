package org.olf.reshare



import grails.gorm.multitenancy.Tenants
import grails.events.annotation.Subscriber
import grails.gorm.multitenancy.WithoutTenant
import grails.gorm.transactions.Transactional

import org.olf.okapi.modules.directory.Service
import com.k_int.web.toolkit.refdata.RefdataValue
import com.k_int.web.toolkit.refdata.RefdataCategory
import com.k_int.web.toolkit.custprops.CustomPropertyDefinition
import com.k_int.web.toolkit.custprops.types.CustomPropertyText;

import org.olf.okapi.modules.directory.NamingAuthority;

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
    log.info("DirectoryHousekeepingService::onSchemaUpdate(${tn},${tid})")
  }

  @Subscriber('okapi:tenant_load_reference')
  public void onTenantLoadReference(final String tenantId,
                                    final String value,
                                    final boolean existing_tenant,
                                    final boolean upgrading,
                                    final String toVersion,
                                    final String fromVersion) {
    log.info("onTenantLoadReference(${tenantId},${value},${existing_tenant},${upgrading},${toVersion},${fromVersion})");
  }

  @Subscriber('okapi:tenant_load_sample')
  public void onTenantLoadSample(final String tenantId, 
                                 final String value, 
                                 final boolean existing_tenant, 
                                 final boolean upgrading, 
                                 final String toVersion, 
                                 final String fromVersion) {
    log.info("onTenantLoadReference(${tenantId},${value},${existing_tenant},${upgrading},${toVersion},${fromVersion})");
  }

}


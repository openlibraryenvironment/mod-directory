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
import com.k_int.okapi.OkapiTenantResolver
import com.k_int.web.toolkit.settings.AppSetting
import com.k_int.web.toolkit.refdata.RefdataValue;
import org.olf.okapi.modules.directory.NamingAuthority;
import grails.databinding.SimpleMapDataBindingSource 
import com.k_int.web.toolkit.custprops.types.CustomPropertyText;
import com.k_int.web.toolkit.custprops.types.CustomPropertyRefdataDefinition
import com.k_int.web.toolkit.custprops.CustomPropertyDefinition


/**
 * This service works at the module level, it's often called without a tenant context.
 */
@Transactional
class DirectoryHousekeepingService {

  def grailsWebDataBinder


  @Subscriber('okapi:tenant_load_reference')
  public void onTenantLoadReference(final String tenantId,
                                    final String value,
                                    final boolean existing_tenant,
                                    final boolean upgrading,
                                    final String toVersion,
                                    final String fromVersion) {
    // log.info("onTenantLoadReference(${tenantId},${value},${existing_tenant},${upgrading},${toVersion},${fromVersion})");
    // Please use the okapi:dataload:reference event below instead o this event.
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

  // @Subscriber('okapi:tenant_enabled')
  @Subscriber('okapi:dataload:reference')
  public void onLoadReference (final String tenantId, String value, final boolean existing_tenant, final boolean upgrading, final String toVersion, final String fromVersion) {
    log.debug("DirectoryHousekeepingService::onLoadReference(${tenantId},${value},${existing_tenant},${upgrading},${toVersion},${fromVersion})");
    final String tenant_schema_id = OkapiTenantResolver.getTenantSchemaName(tenantId)
    try {
      Tenants.withId(tenant_schema_id) {
        // ensureTextProperty('pickup_location_code', false, 'Pickup location code')
        // ensureTextProperty('delivery_stop', false, 'Delivery Stop')
        // ensureTextProperty('print_name', false, 'Print Name')
        // ensureTextProperty('key', false, 'Key')

        AppSetting directory_announce_url = AppSetting.findByKey('directory_announce_url') ?: new AppSetting(
                                  section:'directory',
                                  settingType:'String',
                                  key: 'directory_announce_url',
                                  ).save(flush:true, failOnError: true);


        def cp_ns = ensureTextProperty('ILLPreferredNamespaces', false);
        def cp_url = ensureTextProperty('url', false);
        def cp_demoprop = ensureTextProperty('demoCustprop', false);
        def cp_test_prop = ensureTextProperty('TestParam', false);
        def cp_z3950_base_name = ensureTextProperty('Z3950BaseName', false);
        def cp_local_institutionalPatronId = ensureTextProperty('local_institutionalPatronId', true, label='Institutional patron ID');
        def cp_local_widget2 = ensureTextProperty('local_widget_2', true, label='Widget 2');
        def cp_local_widget3 = ensureTextProperty('local_widget_3', true, label='Widget 3');
        def cp_local_alma_agency = ensureTextProperty('ALMA_AGENCY_ID', true, label='ALMA Agency ID');

        NamingAuthority reshare = NamingAuthority.findBySymbol('RESHARE') ?: new NamingAuthority(symbol:'RESHARE').save(flush:true, failOnError:true);
        NamingAuthority isil = NamingAuthority.findBySymbol('ISIL') ?: new NamingAuthority(symbol:'ISIL').save(flush:true, failOnError:true);
        NamingAuthority oclc = NamingAuthority.findBySymbol('OCLC') ?: new NamingAuthority(symbol:'OCLC').save(flush:true, failOnError:true);
        NamingAuthority exl = NamingAuthority.findBySymbol('EXL') ?: new NamingAuthority(symbol:'EXL').save(flush:true, failOnError:true);
        NamingAuthority palci = NamingAuthority.findBySymbol('PALCI') ?: new NamingAuthority(symbol:'PALCI').save(flush:true, failOnError:true);
        NamingAuthority cardinal = NamingAuthority.findBySymbol('CARDINAL') ?: new NamingAuthority(symbol:'CARDINAL').save(flush:true, failOnError:true);

        RefdataValue.lookupOrCreate('Service.Type', 'ISO18626')
        RefdataValue.lookupOrCreate('Service.Type', 'RTAC')
        RefdataValue.lookupOrCreate('Service.Type', 'HTTP')
        RefdataValue.lookupOrCreate('Service.Type', 'NCIP')
        RefdataValue.lookupOrCreate('Service.Type', 'OAI-PMH')
        RefdataValue.lookupOrCreate('Service.Type', 'Z3950')

        RefdataValue.lookupOrCreate('Service.BusinessFunction', 'ILL')
        RefdataValue.lookupOrCreate('Service.BusinessFunction', 'CIRC')
        RefdataValue.lookupOrCreate('Service.BusinessFunction', 'RTAC')
        RefdataValue.lookupOrCreate('Service.BusinessFunction', 'HARVEST')
        RefdataValue.lookupOrCreate('Service.BusinessFunction', 'RS_STATS')

        RefdataValue.lookupOrCreate('YNO', 'Yes')
        RefdataValue.lookupOrCreate('YNO', 'No')
        RefdataValue.lookupOrCreate('YNO', 'Other')

        // We need to rename "Lendin Physical Only" to Lending Physical Only"
        renameRefdata('LoanPolicy', 'Lendin Physical Only', 'Lending Physical Only');

        RefdataValue.lookupOrCreate('LoanPolicy', 'Lending all types')
        RefdataValue.lookupOrCreate('LoanPolicy', 'Not Lending')
        RefdataValue.lookupOrCreate('LoanPolicy', 'Lending Physical only')
        RefdataValue.lookupOrCreate('LoanPolicy', 'Lending Electronic only')

        def na_reshare = NamingAuthority.findBySymbol('RESHARE') ?: new NamingAuthority(symbol:'RESHARE').save(flush:true, failOnError:true);
        def na_isil = NamingAuthority.findBySymbol('ISIL') ?: new NamingAuthority(symbol:'ISIL').save(flush:true, failOnError:true);
        def na_palci = NamingAuthority.findBySymbol('PALCI') ?: new NamingAuthority(symbol:'PALCI').save(flush:true, failOnError:true);

        // Code, Internal(True=Local/private, False=Global/shared), [optional refdata category]  label
        def cp_accept_returns_policy = ensureRefdataProperty('policy.ill.returns', false, 'YNO', 'Accept Returns' )
        def cp_physical_loan_policy = ensureRefdataProperty('policy.ill.loan_policy', false, 'LoanPolicy', 'ILL Loan Policy' )
        def cp_last_resort_policy = ensureRefdataProperty('policy.ill.last_resort', false, 'YNO', 'Consider Institution As Last Resort' )
        def cp_lb_ratio = ensureTextProperty('policy.ill.InstitutionalLoanToBorrowRatio', false, label='ILL Loan To Borrow Ratio');

      }
    }
    catch ( Exception e ) {
      log.error("Problem in DirectoryHousekeepingService",e);
    }
  }

  private CustomPropertyDefinition ensureTextProperty(String name, boolean local = true, String label = null) {
    CustomPropertyDefinition result = CustomPropertyDefinition.findByName(name) ?: new CustomPropertyDefinition(
                                        name:name,
                                        type:com.k_int.web.toolkit.custprops.types.CustomPropertyText.class,
                                        defaultInternal: local,
                                        label:label
                                      ).save(flush:true, failOnError:true);
    return result;
  }

  private void renameRefdata(String category, String oldValue, String newValue) {
    RefdataCategory categoryRefdata = RefdataCategory.findByDesc(category);
    if (categoryRefdata != null) {
      String normValue = RefdataValue.normValue(oldValue);
      RefdataValue valueRefData = RefdataValue.findByOwnerAndValue(categoryRefdata, normValue);
      if (valueRefData != null) {
        valueRefData.label = newValue;
        valueRefData.value = RefdataValue.normValue(newValue);
        valueRefData.save(flush:true, failOnError:true);
      }
    }
  }

  private CustomPropertyDefinition ensureRefdataProperty(String name, boolean local, String category, String label = null) {
    CustomPropertyDefinition result = null;
    def rdc = RefdataCategory.findByDesc(category);

    if ( rdc != null ) {
      result = CustomPropertyDefinition.findByName(name)
      if ( result == null ) {
        result = new CustomPropertyRefdataDefinition(
                                          name:name,
                                          defaultInternal: local,
                                          label:label,
                                          category: rdc)
        // Not entirely sure why type can't be set in the above, but other bootstrap scripts do this
          // the same way, so copying. Type doesn't work when set as a part of the definition above
        result.type=com.k_int.web.toolkit.custprops.types.CustomPropertyRefdata.class
        result.save(flush:true, failOnError:true);
      }
    }
    else {
      println("Unable to find category ${category}");
    }
    return result;
  }

  /**
   * This is called by the eventing mechanism - There is no web request context
   * this method is called after the schema for a tenant is updated.
   */
  @Subscriber('okapi:schema_update')
  public void onSchemaUpdate(tenantName, tenantId) {
    log.info("DirectoryHousekeepingService::onSchemaUpdate(${tenantName},${tid})")
  }

}


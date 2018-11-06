package org.olf

import com.k_int.okapi.OkapiTenantAwareController
import grails.converters.JSON
import grails.gorm.multitenancy.CurrentTenant
import groovy.util.logging.Slf4j
import com.k_int.web.toolkit.utils.DomainUtils
import org.olf.general.refdata.GrailsDomainRefdataHelpers

import com.k_int.custprops.PropertyDefinition;

@Slf4j
@CurrentTenant
class CustpropsController extends OkapiTenantAwareController<PropertyDefinition>  {

  CustpropsController() {
    super(PropertyDefinition)
  }

}


package org.olf

import grails.gorm.multitenancy.CurrentTenant
import groovy.util.logging.Slf4j
import com.k_int.okapi.OkapiTenantAwareController
import grails.converters.JSON
import org.olf.okapi.modules.directory.ServiceAccount

/**
 * Access to ServiceAccount
 */
@Slf4j
@CurrentTenant
class ServiceAccountController extends OkapiTenantAwareController<ServiceAccount>  {

  ServiceAccountController() {
    super(ServiceAccount)
  }

}


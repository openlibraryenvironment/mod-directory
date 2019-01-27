package org.olf

import grails.gorm.multitenancy.CurrentTenant
import groovy.util.logging.Slf4j
import com.k_int.okapi.OkapiTenantAwareController
import grails.converters.JSON
import org.olf.okapi.modules.directory.Service

/**
 * Access to InternalContact resources
 */
@Slf4j
@CurrentTenant
class ServiceController extends OkapiTenantAwareController<Service>  {

  ServiceController() {
    super(Service)
  }

}


package org.olf

import grails.gorm.multitenancy.CurrentTenant
import groovy.util.logging.Slf4j
import com.k_int.okapi.OkapiTenantAwareController
import grails.converters.JSON
import org.olf.okapi.modules.directory.NamingAuthority

/**
 */
@Slf4j
@CurrentTenant
class NamingAuthorityController extends OkapiTenantAwareController<NamingAuthority>  {

  NamingAuthorityController() {
    super(NamingAuthority)
  }

}


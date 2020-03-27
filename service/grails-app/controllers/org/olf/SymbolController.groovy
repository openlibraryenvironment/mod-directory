package org.olf

import grails.gorm.multitenancy.CurrentTenant
import groovy.util.logging.Slf4j
import com.k_int.okapi.OkapiTenantAwareController
import grails.converters.JSON
import org.olf.okapi.modules.directory.Symbol

/**
 * Access to Symbol resources
 */
@Slf4j
@CurrentTenant
class SymbolController extends OkapiTenantAwareController<Symbol>  {

  SymbolController() {
    super(Symbol)
  }

}


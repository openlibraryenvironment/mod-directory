package org.olf

import grails.gorm.multitenancy.CurrentTenant
import groovy.util.logging.Slf4j
import com.k_int.okapi.OkapiTenantAwareController
import grails.converters.JSON
import org.olf.okapi.modules.directory.Service
import grails.gorm.transactions.Transactional

/**
 * Access to InternalContact resources
 */
@Slf4j
@CurrentTenant
class ServiceController extends OkapiTenantAwareController<Service>  {

  ServiceController() {
    super(Service)
  }

  @Override
  def save() {
    log.debug("ServiceController::save()");
    Service.withTransaction {
      super.save()
    }
  }

  @Override
  def update() {
    log.debug("ServiceController::update()");
    Service.withTransaction {
      super.update()
    }
  }

  @Override
  def delete() {
    log.debug("ServiceController::delete()");
    Service.withTransaction {
      super.delete()
    }
  }

}


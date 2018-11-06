package org.olf

import grails.core.GrailsApplication
import grails.plugins.*
import org.olf.license.DataloadService
import grails.gorm.multitenancy.CurrentTenant
import groovy.util.logging.Slf4j

@Slf4j
@CurrentTenant
class ApplicationController implements PluginManagerAware {

  GrailsApplication grailsApplication
  GrailsPluginManager pluginManager
  DataloadService dataloadService

  def index() {
    [grailsApplication: grailsApplication, pluginManager: pluginManager]
  }

  def config() {
    if ( request.method=='POST' ) {
      log.debug("ApplicationController::config POST");
      if ( request.JSON ) {
        dataloadService.upsertConfigJson(request.JSON);
      }
    }
  }
}

package org.olf

import grails.core.GrailsApplication
import grails.plugins.*
import grails.converters.JSON
import org.olf.reshare.FoafService;
import grails.gorm.multitenancy.Tenants;

class DirectorySettingsController {

  FoafService foafService


  def worker() {
    def result = [result:'OK']
    String tenant_header = request.getHeader('X-OKAPI-TENANT')
    log.info("Worker thread invoked....${tenant_header}");
    request.headerNames.each { hn ->
      log.debug("  ${hn} -> ${request.getHeader(hn)}");
    }
    // backgroundTaskService.performReshareTasks(tenant_header+'_mod_directory');
    render result as JSON
  }

  def foaf() {
    String tenant_header = request.getHeader('X-OKAPI-TENANT')
    log.info("DirectorySettingsController::foaf... ${tenant_header}");
    def result = [result:'OK']
    log.debug("FOAF thread invoked....${tenant_header}");
    foafService.freshen(tenant_header);
    foafService.announce(tenant_header);
    render result as JSON
  }
}



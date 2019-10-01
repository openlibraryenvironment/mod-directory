package org.olf

import grails.core.GrailsApplication
import grails.plugins.*
import grails.converters.JSON
import org.olf.reshare.FoafService;

class DirectorySettingsController {

  FoafService foafService


  def worker() {
    def result = [result:'OK']
    String tenant_header = request.getHeader('X-OKAPI-TENANT')
    log.debug("Worker thread invoked....${tenant_header}");
    // backgroundTaskService.performReshareTasks(tenant_header+'_mod_directory');
    render result as JSON
  }

  def foaf() {
    def result = [result:'OK']
    String tenant_header = request.getHeader('X-OKAPI-TENANT')
    log.debug("FOAF thread invoked....${tenant_header}");
    foafService.freshen();
    render result as JSON
  }
}



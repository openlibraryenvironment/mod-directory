package org.olf

import grails.core.GrailsApplication
import grails.plugins.*
import grails.converters.JSON

class DirectorySettingsController {

  def worker() {
    def result = [result:'OK']
    String tenant_header = request.getHeader('X-OKAPI-TENANT')
    log.debug("Worker thread invoked....${tenant_header}");
    // backgroundTaskService.performReshareTasks(tenant_header+'_mod_directory');
    render result as JSON
  }

}



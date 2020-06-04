package org.olf;

import org.olf.okapi.modules.directory.DirectoryEntry

import grails.core.GrailsApplication
import grails.plugins.*
import grails.converters.JSON
import grails.gorm.multitenancy.Tenants
import java.text.SimpleDateFormat
import groovy.xml.StreamingMarkupBuilder
import grails.gorm.multitenancy.Tenants
import groovy.util.logging.Slf4j

/**
 * External Read-Only APIs for resource sharing network connectivity
 */
@Slf4j
class externalApi {

  GrailsApplication grailsApplication
  GlobalConfigService globalConfigService

  def index() {
  }

  def dirent(String tenant, String slug) {
    log.debug("externalApi::dirent(${tenant},${slug})");

    render [
      status:'OK'
    ] as JSON
  }


}

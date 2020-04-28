package org.olf

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
class ExternalApiController {

  GrailsApplication grailsApplication

  def index() {
    def result =  [
      status:'OK',
    ]
    render result as JSON;
  }

  def directoryIndex(String tenant) {
    def result =  [
      status:'OK',
      tenant: tenant
    ]
    Tenants.withId(tenant) {
      def directoryEntryList = DirectoryEntry.getAll()
      log.debug("DEBUG List of Directory Entries: ${directoryEntryList}")
    }

    render result as JSON;
  }

  def directoryEntry(String tenant) {
    def result =  [
      status:'OK',
      tenant: tenant
    ] 
    render result as JSON;
  }

}

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
import grails.gorm.transactions.Transactional


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
    Tenants.withId(tenant << "_mod_directory") {
      // We only want to return those entries which are listed as "Managed" in this tenant
   
      def directoryEntryList =  DirectoryEntry.createCriteria().list {
        createAlias('status', 'the_status')
        eq 'the_status.value', 'managed'
        isNull("parent")
      }
      log.debug("DEBUG List of managed root Directory Entries ${directoryEntryList}")
      //result.managedEntries = directoryEntryList
    }

    render result as JSON;
  }

  def directoryEntry(String tenant, String slug) {
    def result =  [
      status:'OK',
      tenant: tenant
    ]

     def directoryEntryMatchingSlug = DirectoryEntry.createCriteria().list {
        createAlias('slug', 'the_slug')
        eq 'the_slug', slug
      }
      result.directoryEntry = directoryEntryMatchingSlug

    render result as JSON;
  }

}

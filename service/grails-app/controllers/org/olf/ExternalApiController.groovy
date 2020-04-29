package org.olf

import org.hibernate.FetchMode
import org.olf.okapi.modules.directory.DirectoryEntry

import com.k_int.okapi.OkapiTenantAwareController

import grails.converters.JSON
import grails.core.GrailsApplication
import grails.gorm.multitenancy.CurrentTenant
import grails.gorm.multitenancy.Tenants
import groovy.util.logging.Slf4j


/**
 * External Read-Only APIs for resource sharing network connectivity
 */
@CurrentTenant
class ExternalApiController extends OkapiTenantAwareController<DirectoryEntry> {

  GrailsApplication grailsApplication

  ExternalApiController() {
    super (DirectoryEntry, true)
  }
  
  def index() {
    def result =  [
      status:'OK',
    ]
    render result as JSON;
  }

  def directoryIndex(final String slug) {
    
    respond doTheLookup {
      if (slug) {
        // Filter by slug.
        eq 'slug', slug
      } else {
        // Just output the whole tree, by finding the top-level entry with the status set to managed.
        
        // Should filter by this first as it will greatly reduce the number of results.
        isNull('parent')
        
        createAlias('status', 'the_status')
        eq 'the_status.value', 'managed'


      }
    }

    
    
  }

}

package org.olf

import org.hibernate.FetchMode
import org.olf.okapi.modules.directory.DirectoryEntry

import com.k_int.okapi.OkapiTenantAwareController

import grails.converters.JSON
import grails.core.GrailsApplication
import grails.gorm.multitenancy.CurrentTenant
import grails.gorm.multitenancy.Tenants
import groovy.util.logging.Slf4j
import org.olf.reshare.AppListenerService

/**
 * External Read-Only APIs for resource sharing network connectivity
 */
@CurrentTenant
class ExternalApiController {

  GrailsApplication grailsApplication
  AppListenerService appListenerService

  def index() {
    def result =  [
      status:'OK',
      hostedEntries:[],
      directoryAppMetadata:[
        version: grailsApplication.config?.info?.app?.version,
        buildNumber: grailsApplication.metadata['build.number']
      ]
    ]

    DirectoryEntry.executeQuery('select de from DirectoryEntry as de where de.status.value=:managed and de.parent is null', [managed:'managed']).each { de ->
      result.hostedEntries.add([slug:de.slug, name:de.name, description: de.description]);
    }

    render result as JSON;
  }

  def directoryEntry(final String slug) {

    Map result = null;
    DirectoryEntry de = DirectoryEntry.findBySlug(slug)

    log.debug("Looking up ${slug} - result:${de}");

    // We want a JSON version of the directory entry without any identifiers in it - this is a context free object
    // that other reshare instances will consume, not a JSON object for a local edit screen. appListenerService already
    // exposes a method for this - so reuse it.
    if ( de ) {
      result = appListenerService.makeDirentJSON(de, true, false, true, true);
    }
    else {
      response.sendError(404)
    }
 
    if ( result ) {
      result.directoryAppMetadata = [
        version: grailsApplication.config?.info?.app?.version,
        buildNumber: grailsApplication.metadata['build.number']
      ]
    }
    

    render result as JSON
  }
}

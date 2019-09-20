package org.olf

import grails.core.GrailsApplication
import grails.plugins.*
import grails.gorm.multitenancy.CurrentTenant
import groovy.util.logging.Slf4j
import org.olf.okapi.modules.directory.DirectoryEntry
import grails.converters.JSON
import org.olf.reshare.FoafService;


@Slf4j
@CurrentTenant
class ApplicationController implements PluginManagerAware {

  GrailsApplication grailsApplication
  GrailsPluginManager pluginManager
  FoafService foafService

  def index() {
    [grailsApplication: grailsApplication, pluginManager: pluginManager]
  }

  def config() {
    if ( request.method=='POST' ) {
      log.debug("ApplicationController::config POST");
    }
  }

  def findSymbol() {
    log.debug("findSymbol() ${params}");
    def result = [:]
    DirectoryEntry directory_entry = DirectoryEntry.get(params.'for');
    if ( directory_entry ) {
      // got entry
      log.debug("Located directory entry ${directory_entry} - symbols ${directory_entry.symbols.collect { it.symbol } }");
      result.symbol = directory_entry.locateSymbolInNamespace(params.ns)
    }
    else {
      result.message = "Unable to locate directory entry ${params.'for'}";
    }

    render result as JSON
  }

  def addFriend() {
    def result=[status:'OK']
    log.debug("ApplicationController::addFriend(${params})");
    if ( params.friendUrl ) {
      foafService.checkFriend(params.friendUrl);
    }
    render result as JSON
  }
}

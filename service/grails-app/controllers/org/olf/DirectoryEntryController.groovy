package org.olf

import grails.gorm.multitenancy.CurrentTenant
import groovy.util.logging.Slf4j
import com.k_int.okapi.OkapiTenantAwareController
import grails.converters.JSON
import org.olf.okapi.modules.directory.DirectoryEntry
import grails.gorm.transactions.Transactional
import com.k_int.okapi.OkapiHeaders
import org.grails.web.json.JSONObject
import org.grails.web.json.JSONArray
import groovy.json.JsonOutput;

import groovy.json.*


/**
 * Access to InternalContact resources
 */
@Slf4j
@CurrentTenant
class DirectoryEntryController extends OkapiTenantAwareController<DirectoryEntry>  {
  
  final UPDATE_LOCAL_PERMISSION = "directory.entry.item-local.put";
  final UPDATE_SHARED_PERMISSION = "directory.entry.item-shared.put";

  DirectoryEntryController() {
    super(DirectoryEntry)
  }

  @Override
  def save() {
    DirectoryEntry.withTransaction {
      def data = getObjectToBind()
      if (data.parent?.id) {
        // We have a parent, check if it's managed
        def parentDirEnt = DirectoryEntry.read(data.parent.id)
        if (parentDirEnt.status?.value != 'managed') {
          response.sendError(403, "Cannot create a unit for a non-managed DirectoryEntry")
          return;
        }
      }

      // Do what the superclass would have done anyway
      super.save()
    }
  }

  @Override
  @Transactional
  def update() {
    DirectoryEntry.withTransaction {
      log.debug("Overridden DirectoryEntryController::update() - called when there is a put on a directory entry resource");
      
      if ( request.JSON != null ) {
        // If we are manually updating a directory entry, then it must be locally managed.
        // Setting this manually will force an update event at the directory entry 
        // level even if a child property such as a custprop has been set This will
        // subsequently trigger a directory entry updated event in kafka and cause
        // an updated record to be issued.
        request.JSON.pubLastUpdate = System.currentTimeMillis();
      }
      
      if( !patron ) {
        //return unauthorized
        render status:401
        return;
      }

      // Rely on AppListener to deal with permissions for updates
      super.update();
    }
  }
}


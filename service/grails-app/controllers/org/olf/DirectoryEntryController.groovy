package org.olf

import grails.gorm.multitenancy.CurrentTenant
import groovy.util.logging.Slf4j
import com.k_int.okapi.OkapiTenantAwareController
import grails.converters.JSON
import org.olf.okapi.modules.directory.DirectoryEntry
import com.k_int.okapi.OkapiHeaders
import org.grails.web.json.JSONObject
import org.grails.web.json.JSONArray


/**
 * Access to InternalContact resources
 */
@Slf4j
@CurrentTenant
class DirectoryEntryController extends OkapiTenantAwareController<DirectoryEntry>  {
  
  final UPDATE_LOCAL_PERMISSION = "directory.entry.item-local.put";
  final UPDATE_MANAGED_PERMISSION = "directory.entry.managed-item.put";
  final UPDATE_ANY_PERMISSION = "directory.entry.item.put";

  DirectoryEntryController() {
    super(DirectoryEntry)
  }

  @Override
  def save() {
    DirectoryEntry.withTransaction {
      log.debug("Overridden DirectoryEntryController::save() - called when there is a post on a directory entry resource");

      // Here is one way to get hold of the permissions
      String okapi_permissions_str = request.getHeader(OkapiHeaders.PERMISSIONS) ?: '[]';
      log.debug("Permissions for this request are: ${okapi_permissions_str}");

      // But the superclass should be parsing that and surfacing the permissions so that
      // request.isUserInRole("okapi.directory.entry.item.update") 
      // N.B. 1 The okapi. prefix which distinguishes OKAPI permissions from other spring security perms
      // N.B. 2 You can also use the @Secured({"okapi.a.b.c"}) at the method level but the conditional nature of
      // the requirement might mean it's cleaner to do this in the body of the method.

      // Do what the superclass would have done anyway
      super.save()
    }
  }

  @Override
  def update() {
    DirectoryEntry.withTransaction {
      log.debug("Overridden DirectoryEntryController::update() - called when there is a put on a directory entry resource");
      
      String okapi_permissions_str = request.getHeader(OkapiHeaders.PERMISSIONS) ?: '[]';
      JSONArray permsArray
      try {
        permsArray = new JSONArray(okapi_permissions_str);
      } catch(Exception e) {
        log.debug("Unable to parse ${okapi_permissions_str} into array: ${e.getMessage()}");
        permsArray = new JSONArray();
      }
      log.debug("Permissions for this request are: ${okapi_permissions_str}");
     
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
      
      Boolean updateLocal = permsArray.contains(UPDATE_LOCAL_PERMISSION);
      Boolean updateManaged = permsArray.contains(UPDATE_MANAGED_PERMISSION);
      Boolean updateAny = permsArray.contains(UPDATE_ANY_PERMISSION);
      //Boolean updateAny = request.isUserInRole("okapi." + UPDATE_ANY_PERMISSION);
      
      if(!updateAny) {
        DirectoryEntry originalEntry = queryForResource(params.id);
        if(originalEntry.status?.value == 'managed' && updateManaged) {
          log.debug("Managed update permitted for entry ${params.id}");
          super.update();
          return;
        }
        if(updateLocal) {
          log.debug("Only updating 'customProperties' for entry ${params.id}");
          newJSON = new JSONObject();
          newJSON.customProperties = request.JSON?.customProperties;
          newJson.id = request.JSON?.id;
          request.JSON = newJSON;
          super.update();
          return;
        }
        //Return a 401
        log.debug("Insufficient permissions to update entry ${params.id}");
        render status:401
        return;
      }
      log.debug("Full update permissions for directory");
      super.update();
    }
  }
  

}


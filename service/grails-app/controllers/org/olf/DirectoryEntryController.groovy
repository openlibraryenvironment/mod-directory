package org.olf

import grails.gorm.multitenancy.CurrentTenant
import groovy.util.logging.Slf4j
import com.k_int.okapi.OkapiTenantAwareController
import grails.converters.JSON
import org.olf.okapi.modules.directory.DirectoryEntry
import com.k_int.okapi.OkapiHeaders

/**
 * Access to InternalContact resources
 */
@Slf4j
@CurrentTenant
class DirectoryEntryController extends OkapiTenantAwareController<DirectoryEntry>  {

  DirectoryEntryController() {
    super(DirectoryEntry)
  }

  @Override
  def save() {
    DirectoryEntry.withTransaction {
      log.debug("Overridden DirectoryEntryController::save() - called when there is a put on a directory entry resource");

      // Here is one way to get hold of the permissions
      String okapi_permissions_str = request.getHeader(OkapiHeaders.PERMISSIONS) ?: '[]'

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
      log.debug("Overridden DirectoryEntryController::update() - called when there is a post on a directory entry resource");
      if ( request.JSON != null ) {
        // If we are manually updating a directory entry, then it must be locally managed. Setting this manually
        // will force an update event at the directory entry level even if a child property such as a custprop has been set
        // This will subsequently trigger a directory entry updated event in kafka and cause an updated record to be issued.
        request.JSON.pubLastUpdate = System.currentTimeMillis();
      }

      super.update()
    }
  }

}


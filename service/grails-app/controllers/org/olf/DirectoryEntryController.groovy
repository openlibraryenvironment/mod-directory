package org.olf

import grails.gorm.multitenancy.CurrentTenant
import groovy.util.logging.Slf4j
import grails.converters.JSON
import org.olf.okapi.modules.directory.DirectoryEntry;
import org.olf.okapi.modules.directory.NamingAuthority;
import org.olf.okapi.modules.directory.Symbol;
import com.k_int.okapi.OkapiHeaders
import org.grails.web.json.JSONObject
import org.grails.web.json.JSONArray;
import groovy.json.JsonOutput;

import com.k_int.web.toolkit.refdata.RefdataValue

/**
 * Access to InternalContact resources
 */
@Slf4j
@CurrentTenant
class DirectoryEntryController extends OkapiTenantAwareExtendedController<DirectoryEntry>  {

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
		// Check the symbols before we do anything else
		String symbolError = checkSymbols(request.JSON.symbols);
		if (symbolError != null) {
			render(status : 400, contentType: "application/json", text : JsonOutput.toJson([error : 400, message :symbolError]));
			return;
		}

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
      DirectoryEntry originalEntry = queryForResource(params.id);

      if(!updateAny) {
        if(originalEntry.status?.value == 'managed' && updateManaged) {
          log.debug("Managed update permitted for entry ${params.id}");
          super.update();
          return;
        }
        if(updateLocal) {
          log.debug("Only updating 'customProperties' for entry ${params.id}");
          def keys = request.JSON.keys();
          def badKeyList = []
          for(String key : keys) {
            log.debug("Key is ${key}");
            if(! (key.equals("id") || key.equals("customProperties") || key.equals("pubLastUpdate")) ) {
              badKeyList.add(key)
            }
          }
          for(String key : badKeyList) {
            request.JSON.put(key, null);
            log.debug("Setting key key ${key} from request.JSON to null");
          }
          //now we need to remove any members from customProperties that are not "local"
          def customProps = request.JSON["customProperties"];
          if(customProps) {
            def cpKeys = customProps.keys();
            def badCPKeys = []
            for(cpKey in cpKeys) {
              if(isNotLocalProperty(cpKey, originalEntry)) {
                badCPKeys.add(cpKey);
              }
            }
            for(badCPKey in badCPKeys) {
              request.JSON["customProperties"].remove(badCPKey);
              log.debug("Removing entry ${badCPKey} from customProperties PUT data");
            }
          }
          log.debug("The result of request.JSON is ${JsonOutput.toJson(request.JSON)}");
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

  def boolean isNotLocalProperty(propName, entry) {
    for(cpValue in entry.customProperties.value) {
      if(!propName.equals(cpValue.definition.name)) {
        log.debug("${propName} not equal to ${cpValue.definition.name}")
        continue;
      }
      log.debug("defaultInternal for ${propName} is ${cpValue.definition.defaultInternal}")
      if(cpValue.definition.defaultInternal == false) {
        return true;
      }
    }
    return false;
  }

  private String checkSymbols(def symbols) {

	  // A null result means no errors
	  String result = null;

	  // Loop through all the symbols
	  if (symbols != null) {
		  // The symbols should be an array
		  if (symbols instanceof JSONArray) {
			  symbols.each { symbol ->
				  Symbol exists = null;

				  // Need to lookup the naming authority first in order to use it in findBy
				  NamingAuthority authority = NamingAuthority.get(symbol.authority.id);

				  // if we are already have an Id we need to exclude records with the current id
				  if (symbol.id) {
					  // We have an id, so exclude
					  exists = Symbol.findBySymbolAndAuthorityAndIdNotEqual(symbol.symbol, authority, symbol.id);
				  } else {
					  // No id
					  exists = Symbol.findBySymbolAndAuthority(symbol.symbol.toUpperCase(), authority);
				  }

				  // if we are already have an Id we need to exclude records with the current id
				  if (exists != null) {
					  // Symbol already exists, so return an appropriate error message
					  if (result == null) {
						  result = "";
					  }

					  // Now give a sensible error
					  result += "The symbol " + exists.symbol + " for authority " + exists.authority.symbol + " already exists for " + exists.owner.name + ". ";
				  }
			  }
		  } else {
			  result = "Symbols not supplied as an array";
		  }
	  }

	  // Return the result to the caller
	  return(result);
  }

  /* This action will be used to provide real time validation information
   * which the client can then act on as it wishes.
   * For example, if the user is attempting to create a root consortium entry
   * when one already exists in the system, this method will return a warning.
   *
   * This could be extended in future to warn when a user is attempting to
   * set up a consortium as a unit, or include logic to detect possible duplications and warn
   * about those, etc.
   */
  public def validate() {
    // Store list of errors/warnings for this directoryEntry
    def returnMap = [
      errors: [],
      warnings: []
    ]

    def directoryEntry = getObjectToBind();
    // Translate 'type' from id to human readable value
    def typeString
    if (directoryEntry.type) {
      typeString = RefdataValue.read(directoryEntry.type)?.value
    }

    switch (typeString) {
      case 'consortium':
        def typeCount = DirectoryEntry.executeQuery(
          """
            SELECT COUNT(dirEnt) from DirectoryEntry dirEnt
            WHERE
              dirEnt.type.value = 'consortium'
          """.toString()
        );
        // If a consortium already exists, warn the user
        if (!directoryEntry.parent && typeCount[0] > 0) {
          returnMap.warnings << "consortiumAlreadyExists"
        }
        break;
      case 'institution':
        def typeCount = DirectoryEntry.executeQuery(
          """
            SELECT COUNT(dirEnt) from DirectoryEntry dirEnt
            WHERE
              dirEnt.status.value = 'managed' AND
              dirEnt.type.value = 'institution'
          """.toString()
        );
        // If a managed institution already exists, warn the user
        if (!directoryEntry.parent && typeCount[0] > 0) {
          returnMap.warnings << "managedInstitutionAlreadyExists"
        }
        break;
      case 'branch':
        break;
      default:
        break;
    }

    respond returnMap;
  }
}


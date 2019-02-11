package org.olf.okapi.modules.directory

import grails.gorm.MultiTenant
import com.k_int.web.toolkit.custprops.CustomProperties
import com.k_int.web.toolkit.custprops.types.CustomPropertyContainer
import com.k_int.web.toolkit.tags.Tag
import grails.gorm.MultiTenant
import com.k_int.web.toolkit.refdata.RefdataValue;
import com.k_int.web.toolkit.refdata.Defaults;



class DirectoryEntry  implements MultiTenant<DirectoryEntry>,CustomProperties  {

  String id
  String name
  String slug
  String description
  DirectoryEntry parent

  /**
   * DirectoryEntries can be managed here, or just stored for reference. Managed entries can be accessed via
   * a foaf type service.
   */
  @Defaults(['Managed', 'Reference' ])
  RefdataValue status

  static graphql = true

  static hasMany = [
    tags:Tag,
    friends: FriendAssertion,
    units: DirectoryEntry,
    symbols: Symbol,
    services: ServiceAccount,
    announcements: Announcement
  ]

  static mappedBy = [
    friends: 'owner',
    units: 'parent',
    symbols: 'owner',
    services: 'accountHolder',
    announcements: 'owner',
  ]

  static mapping = {
                 id column:'de_id', generator: 'uuid', length:36
               name column:'de_name'
               slug column:'de_slug'
        description column:'de_desc'
             parent column:'de_parent'
             status column:'de_status_fk'
  }

  static constraints = {
           name(nullable:false, blank:false)
           slug(nullable:true, blank:false)
    description(nullable:true, blank:false)
         parent(nullable:true, blank:false)
         status(nullable:true, blank:false)

  }

  /**
   * Search the symbols attached to this entry to see if we can find one in the nominated
   * namespace, if not and if there is a parent, try the parent
   */
  public String locateSymbolInNamespace(String ns) {
    String result = null;
    Symbol located_symbol = symbols.find { it.authority.symbol == ns }

    // If we managed to find a symbol in that namespace, job done, return it
    if ( located_symbol ) {
      result = located_symbol.symbol
    }
    else {
      // We didn't, does this entry have a parent?
      if ( parent ) {
        // Yes, see if the parent has a symbol matching that NS
        result = parent.locateSymbolInNamespace(ns);
      }
    }

    return result
  }

  public String getTagSummary() {
    return tags?.collect {it.value}.join(', ')
  }

  public String getSymbolSummary() {
    return symbols?.collect {it.authority.symbol+':'+it.symbol}.join(', ')
  }

  public String getFullyQualifiedName() {
    String result = null;
    if ( parent ) {
      result = parent.getFullyQualifiedName() + ' / ' + name
    }
    else {
      result = name
    }
    return result;
  }
}

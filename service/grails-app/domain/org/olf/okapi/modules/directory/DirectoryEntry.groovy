package org.olf.okapi.modules.directory

import grails.gorm.MultiTenant


class DirectoryEntry  implements MultiTenant<DirectoryEntry>  {

  String id
  String name
  String slug
  String description

  static mapping = {
                 id column:'de_id', generator: 'uuid', length:36
               name column:'de_name'
               slug column:'de_slug'
        description column:'de_desc'
  }

  static constraints = {
           name(nullable:false, blank:false)
           slug(nullable:true, blank:false)
    description(nullable:true, blank:false)
  }
}

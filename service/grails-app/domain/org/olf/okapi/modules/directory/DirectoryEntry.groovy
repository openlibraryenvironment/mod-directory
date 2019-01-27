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

  static graphql = true

  static hasMany = [
    tags:Tag
  ]

  static mappedBy = [
  ]

  static mapping = {
                 id column:'de_id', generator: 'uuid', length:36
               name column:'de_name'
               slug column:'de_slug'
        description column:'de_desc'
             parent column:'de_parent'
  }

  static constraints = {
           name(nullable:false, blank:false)
           slug(nullable:true, blank:false)
    description(nullable:true, blank:false)
         parent(nullable:true, blank:false)
  }
}

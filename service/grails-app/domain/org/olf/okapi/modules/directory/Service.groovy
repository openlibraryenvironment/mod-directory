package org.olf.okapi.modules.directory

import grails.gorm.MultiTenant


/**
 * A service represents an internet callable endpont. A service can support many
 * accounts or tenants. For example, an iso 10161 service endpoint might support 
 * symbols from multiple different institutions. This class then models the service
 * itself.
 */
class Service  implements MultiTenant<Service>  {

  String id
  String name

  static graphql = true



  static mapping = {
                 id column:'se_id', generator: 'uuid', length:36
               name column:'se_name'
  }

  static constraints = {
           name(nullable:false, blank:false)
  }
}

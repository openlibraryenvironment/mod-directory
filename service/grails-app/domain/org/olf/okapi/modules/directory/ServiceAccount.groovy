package org.olf.okapi.modules.directory

import grails.gorm.MultiTenant


/**
 * The relationship between a service and a directory entry
 */
class ServiceAccount  implements MultiTenant<ServiceAccount>  {

  Service service
  DirectoryEntry accountHolder

  String accountDetails

  static graphql = true



  static mapping = {
                 id column:'sa_id', generator: 'uuid', length:36
            service column:'sa_service'
      accountHolder column:'sa_account_holder'
     accountDetails column:'sa_account_details'
  }

  static constraints = {
           service(nullable:false, blank:false)
     accountHolder(nullable:false, blank:false)
     accountDetails(nullable:true, blank:false)
  }
}

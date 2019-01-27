package org.olf.okapi.modules.directory

import grails.gorm.MultiTenant
import com.k_int.web.toolkit.custprops.CustomProperties
import com.k_int.web.toolkit.custprops.types.CustomPropertyContainer
import com.k_int.web.toolkit.tags.Tag
import grails.gorm.MultiTenant
import com.k_int.web.toolkit.refdata.RefdataValue;
import com.k_int.web.toolkit.refdata.Defaults;



/**
 * A service represents an internet callable endpont. A service can support many
 * accounts or tenants. For example, an iso 10161 service endpoint might support 
 * symbols from multiple different institutions. This class then models the service
 * itself.
 */
class Service  implements CustomProperties,MultiTenant<Service>  {

  String id
  String name

  @Defaults(['Z3950',
             'ISO10161.TCP',
             'ISO10161.SMTP',
             'ISO18626',
             'GSM.SMTP', 
             'OAI-PMH', 
             'SRU', 
             'SRW'])
  RefdataValue type

  static graphql = true

  static hasMany = [
    tags:Tag
  ]

  static mappedBy = [
  ]

  static mapping = {
                 id column:'se_id', generator: 'uuid', length:36
               name column:'se_name'
               type column:'se_type_fk'
  }

  static constraints = {
           name(nullable:false, blank:false)
           type(nullable:false, blank:false)
  }
}

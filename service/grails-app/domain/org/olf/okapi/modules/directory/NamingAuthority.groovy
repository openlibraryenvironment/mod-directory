package org.olf.okapi.modules.directory

import grails.gorm.MultiTenant
import com.k_int.web.toolkit.custprops.CustomProperties
import com.k_int.web.toolkit.custprops.types.CustomPropertyContainer
import com.k_int.web.toolkit.tags.Tag
import grails.gorm.MultiTenant
import com.k_int.web.toolkit.refdata.RefdataValue;
import com.k_int.web.toolkit.refdata.Defaults;

class NamingAuthority implements MultiTenant<NamingAuthority>  {

  String id
  String symbol

  static hasMany = [
  ]

  static mappedBy = [
  ]

  static mapping = {
                 id column:'na_id', generator: 'uuid', length:36
             symbol column:'na_symbol'
  }

  static constraints = {
           symbol(nullable:false, blank:false)
  }
}

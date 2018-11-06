package com.k_int.refdata;

import grails.gorm.MultiTenant

class RefdataCategory implements MultiTenant<RefdataCategory> {

  String id
  String desc
  String label
  Set values

  static mapping = {
         id column:'rdc_id', generator: 'uuid', length:36
    version column:'rdc_version'
      label column:'rdc_label'
       desc column:'rdc_description', index:'rdc_description_idx'
     values sort:'value', order:'asc'

  }

  static hasMany = [
    values:RefdataValue
  ]

  static mappedBy = [
    values:'owner'
  ]

  static constraints = {
    label(nullable:true, blank:true)
  }

  static RefdataValue lookupOrCreate(category_name, value) {
    return lookupOrCreate(category_name,value,null);
  }

  static RefdataValue lookupOrCreate(category_name, value, sortkey) {
  
    if ( value == null )
      throw new RuntimeException("Request to lookupOrCreate null value in category ${category_name}");

    // The category.
    def cat = RefdataCategory.findByDesc(category_name);
    if ( !cat ) {
      cat = new RefdataCategory(desc:category_name)
      cat.save(failOnError:true)
    }

    // II Commented out the following - Seems to clash with domain class extender!
    def result = RefdataValue.findByOwnerAndValueIlike(cat, value)
  
    // SO: Changed this slightly to do a case-insensitive value match.
    //def result = RefdataValue.findAllWhere (owner:cat).find { RefdataValue val ->
    //    val.getValue().equalsIgnoreCase(value)
    //  }

    if ( !result ) {
      // Create and save a new refdata value.
      result = new RefdataValue(owner:cat, value:value, sortKey:sortkey)
      result.save(failOnError:true, flush:true)
    }

    // return the refdata value.
    result
  }

  static String getOID(category_name, value) {
    String result = null
    def cat = RefdataCategory.findByDesc(category_name);
    if ( cat != null ) {
      def v = RefdataValue.findByOwnerAndValueIlike(cat, value)
      if ( v != null ) {
        result = "RefdataValue:${v.id}"
      }
    }
  }

}

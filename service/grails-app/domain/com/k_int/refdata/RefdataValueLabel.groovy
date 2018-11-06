package com.k_int.refdata;

import org.hibernate.proxy.HibernateProxy

import grails.gorm.MultiTenant

class RefdataValueLabel implements MultiTenant<RefdataValueLabel> {

  String id
  RefdataValue owner
  String locale
  String label

  static belongsTo = [
    owner:RefdataValue
  ]

  static mapping = {
    id column: 'rdvl_id', generator: 'uuid', length:36
    version column:'rdvl_version'
    owner column:'rdvl_owner', index:'rdvl_entry_idx'
    locale column:'rdvl_locale', index:'rdvl_entry_idx'
    value column:'rdvl_label'
  }

  static constraints = {
  }

  @Override
  public String toString() {
    return "${label}"
  }

}

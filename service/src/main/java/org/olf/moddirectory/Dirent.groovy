package org.olf.moddirectory;

import grails.gorm.annotation.Entity;
import org.grails.datastore.gorm.GormEntity;
import groovy.transform.CompileStatic

@CompileStatic
@Entity 
class Dirent implements GormEntity<Dirent> { 

    String name;

    static constraints = {
        name nullable: false, blank: false
    }

    static mapping = {
    }
}

	

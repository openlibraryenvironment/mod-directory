package org.olf

import com.k_int.okapi.OkapiTenantAwareController
import com.k_int.web.toolkit.tags.Tag

import grails.gorm.multitenancy.CurrentTenant
import groovy.util.logging.Slf4j

@Slf4j
@CurrentTenant
class TagsController extends OkapiTenantAwareController<Tag> {
  
  TagsController() {
    super(Tag)
  }
}

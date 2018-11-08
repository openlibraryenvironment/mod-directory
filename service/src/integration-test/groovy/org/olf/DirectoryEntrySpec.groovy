package org.olf

import grails.testing.mixin.integration.Integration
import grails.transaction.*
import static grails.web.http.HttpHeaders.*
import static org.springframework.http.HttpStatus.*
import spock.lang.*
import geb.spock.*
import grails.plugins.rest.client.RestBuilder
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.k_int.okapi.OkapiHeaders
import spock.lang.Shared
import grails.gorm.multitenancy.Tenants
import org.olf.okapi.modules.directory.DirectoryEntry


@Integration
@Stepwise
class DirectoryEntrySpec extends GebSpec {

  @Shared
  private Map test_info = [:]

  final Closure authHeaders = {
    header OkapiHeaders.TOKEN, 'dummy'
    header OkapiHeaders.USER_ID, 'dummy'
    header OkapiHeaders.PERMISSIONS, '[ "directory.admin", "directory.user", "directory.own.read", "directory.any.read"]'
  }

  final static Logger logger = LoggerFactory.getLogger(DirectoryEntrySpec.class);

  def setup() {
  }

  def cleanup() {
  }

  // Set up a new tenant called RSTestTenantA
  void "Set up test tenants "(tenantid, name) {
    when:"We post a new tenant request to the OKAPI controller"

      logger.debug("Post new tenant request for ${tenantid} to ${baseUrl}_/tenant");

      def resp = restBuilder().post("${baseUrl}_/tenant") {
        header 'X-Okapi-Tenant', tenantid
        authHeaders.rehydrate(delegate, owner, thisObject)()
      }

    then:"The response is correct"
      resp.status == OK.value()
      // resp.headers[CONTENT_TYPE] == ['application/json;charset=UTF-8']
      // resp.json.message == 'Welcome to Grails!'

    where:
      tenantid | name
      'TestTenantG' | 'TestTenantG'
  }

  void "test directory entry creation"(tenantid, name) {
    when:
      def dirent = null;
      Tenants.withId(tenantid.toLowerCase()+'_mod_directory') {
        dirent = DirectoryEntry.findByName(name) ?: new DirectoryEntry(name:name, slug:name).save(flush:true, failOnError:true);
      }


    then:
      dirent.name == name

    where:
      tenantid | name
      'TestTenantG' | 'University of DIKU'

  }

  void "Delete the tenants"(tenant_id, note) {

    expect:"post delete request to the OKAPI controller for "+tenant_id+" results in OK and deleted tennant"
      def resp = restBuilder().delete("$baseUrl/_/tenant") {
        header 'X-Okapi-Tenant', tenant_id
        authHeaders.rehydrate(delegate, owner, thisObject)()
      }

      logger.debug("completed DELETE request on ${tenant_id}");
      resp.status == OK.value()

    where:
      tenant_id | note
      'TestTenantG' | 'note'
  }

   RestBuilder restBuilder() {
        new RestBuilder()
    }

}


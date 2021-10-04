package org.olf

import grails.testing.mixin.integration.Integration
import grails.transaction.*
import static grails.web.http.HttpHeaders.*
import static org.springframework.http.HttpStatus.*
import spock.lang.*
import geb.spock.*
import groovy.util.logging.Slf4j
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.k_int.okapi.OkapiHeaders
import spock.lang.Shared
import grails.gorm.multitenancy.Tenants
import org.olf.okapi.modules.directory.DirectoryEntry
import com.k_int.web.toolkit.testing.HttpSpec
import static groovyx.net.http.ContentTypes.JSON


@Slf4j
@Integration
@Stepwise
class DirectoryEntrySpec extends HttpSpec {

  @Shared
  private Map test_info = [:]

  def grailsApplication

  Closure authHeaders = {
    header OkapiHeaders.TOKEN, 'dummy'
    header OkapiHeaders.USER_ID, 'dummy'
    header OkapiHeaders.PERMISSIONS, '[ "directory.admin", "directory.user", "directory.own.read", "directory.any.read"]'
  }

  def setup() {
  }

  def cleanup() {
  }

  void "Attempt to delete any old tenant"(tenantid, name) {
    when:"We post a delete request"
      try {
        setHeaders(['X-Okapi-Tenant': tenantid])
        def resp = doDelete("${baseUrl}_/tenant".toString(),null)
      }
      catch ( Exception e ) {
        // If there is no TestTenantG we'll get an exception here, it's fine
      }

    then:"Any old tenant removed"
      1==1

    where:
      tenantid | name
      'TestTenantG' | 'TestTenantG'

  }

  // Set up a new tenant called RSTestTenantA
  void "Set up test tenants "(tenantid, name) {
    when:"We post a new tenant request to the OKAPI controller"

      log.debug("Post new tenant request for ${tenantid} to ${baseUrl}_/tenant");

      setHeaders(['X-Okapi-Tenant': tenantid])
      def resp = doPost("${baseUrl}_/tenant") {
        // header 'X-Okapi-Tenant', tenantid
        authHeaders.rehydrate(delegate, owner, thisObject)()
      }

    log.debug("Got response: ${resp}");
    then:"The response is correct"
      resp != null;

    where:
      tenantid | name
      'TestTenantG' | 'TestTenantG'
  }

  void "test directory entry creation"(tenantid, name) {

    log.debug("Sleep 2s to see that schema creation went OK - running test for ${tenantid} ${name}");

    // Switching context, just want to make sure that the schema had time to finish initialising.
    Thread.sleep(2000)

    Map new_entry = [
          id: java.util.UUID.randomUUID().toString(),
          name:name,
          slug:name,
          description:'Test new entry',
          status:'managed'
    ]

    when: "We create a new directory entry"
      log.debug("Attempt to post ${new_entry}");

      def resp = httpClient.post {
        request.uri = "$baseUrl/directory/entry".toString()
        request.contentType = JSON[0]
        request.body = new_entry
        request.headers = (specDefaultHeaders + headersOverride + ['X-Okapi-Tenant': tenantid])
      }

    then: "New directory entry created with the given name"
      log.debug("Got response ${resp}");
      resp != null;


    where:
      tenantid | name
      'TestTenantG' | 'University of DIKU'
  }



  void "add Friend"(tenant_id, friend_url) {

    log.debug("Add a friend");

    when: "We add a new friend"
      def dirent = null;
      setHeaders(['X-Okapi-Tenant': tenant_id])
      def resp = httpClient.get {
        request.uri = "$baseUrl/directory/entry".toString()
        request.contentType = JSON[0]
        request.headers = (specDefaultHeaders + headersOverride + ['X-Okapi-Tenant': tenant_id])
      }
      // def resp = doGet("$baseUrl/directory/api/addFriend?friendUrl=$friend_url".toString()) {
      //   authHeaders.rehydrate(delegate, owner, thisObject)()
      //   accept 'application/json'
      // }

    then: "New directory entry created with the given name"
      // dirent.name == name
      log.debug("Add friend response: ${resp}");
      resp != null

      // Give the out of band kafka event enough time to propagate
      Thread.sleep(2000);

    where:
      tenant_id | friend_url
      'TestTenantG' | 'https://raw.githubusercontent.com/openlibraryenvironment/mod-directory/master/seed_data/test/test_cons.json'
  }





  // Check parent loop failure
  void "Check Parent loop fails"(tenantid, heirachy, expected, runthrough) {
    log.debug("Checking parent loop");
    log.debug("========================================== runthrough ${runthrough} ==========================================")
    
    when: "We create some directory entries and set their parent structure"
      def dir1 = null;
      def dir2 = null;
      def dir3 = null;
      def dir4 = null;
      def dir5 = null;
      def dir6 = null;
    // Make some Directory Entries
    Tenants.withId(tenantid.toLowerCase()+'_mod_directory') {
      DirectoryEntry.withTransaction {
        dir1 = DirectoryEntry.findByName('dir1') ?: new DirectoryEntry(id:'dir1', name:'dir1', slug:'dir1').save(flush:true, failOnError:true);
      }
    }
    Tenants.withId(tenantid.toLowerCase()+'_mod_directory') {
      DirectoryEntry.withTransaction {
        dir2 = DirectoryEntry.findByName('dir2') ?: new DirectoryEntry(id:'dir2', name:'dir2', slug:'dir2').save(flush:true, failOnError:true);
      }
    }
    Tenants.withId(tenantid.toLowerCase()+'_mod_directory') {
      DirectoryEntry.withTransaction {
        dir3 = DirectoryEntry.findByName('dir3') ?: new DirectoryEntry(id:'dir3', name:'dir3', slug:'dir3').save(flush:true, failOnError:true);
      }
    }
    Tenants.withId(tenantid.toLowerCase()+'_mod_directory') {
      DirectoryEntry.withTransaction {
        dir4 = DirectoryEntry.findByName('dir4') ?: new DirectoryEntry(id:'dir4', name:'dir4', slug:'dir4').save(flush:true, failOnError:true);
      }
    }
    Tenants.withId(tenantid.toLowerCase()+'_mod_directory') {
      DirectoryEntry.withTransaction {
        dir5 = DirectoryEntry.findByName('dir5') ?: new DirectoryEntry(id:'dir5', name:'dir5', slug:'dir5').save(flush:true, failOnError:true);
      }
    }
    Tenants.withId(tenantid.toLowerCase()+'_mod_directory') {
      DirectoryEntry.withTransaction {
        dir6 = DirectoryEntry.findByName('dir6') ?: new DirectoryEntry(id:'dir6', name:'dir6', slug:'dir6').save(flush:true, failOnError:true);
      }
    }

    // Change list of strings to be the ids of entries defined above (choosing id so that we can easily pass these between sessions)
    for (def i=0; i < heirachy.size(); i++){
      Tenants.withId(tenantid.toLowerCase()+'_mod_directory') {
        DirectoryEntry.withTransaction {
          heirachy[i] = (DirectoryEntry.findByName(heirachy[i])).id
        }
      }
    }

    // Set parents of those entries one by one.
    def parentValidation = null
    try {
      for (def i = 0; i < heirachy.size(); i++) {
        Tenants.withId(tenantid.toLowerCase()+'_mod_directory') {
          DirectoryEntry.withTransaction {
            def dirent = DirectoryEntry.get(heirachy[i])
            if (i + 1 < heirachy.size()) {
              dirent.parent = DirectoryEntry.get(heirachy[i + 1])
              dirent.save(flush:true, failOnError: true);
            }
          }
        }
      }
      parentValidation = 'succeeds'
    }
    catch(grails.validation.ValidationException e) {
      log.debug("An error has occured: ${e}")
        parentValidation = 'fails because loop'
    }
    catch (Exception e) {
      log.debug("An error has occured: ${e}")
      parentValidation = 'fails otherwise'
    }


    then: "Check that we get the expected validation failures"
    log.debug("Checking validator. Expected: ${expected}, Validation: ${parentValidation}")
    expected == parentValidation

    // Give the out of band kafka event enough time to propagate
    Thread.sleep(2000);

    log.debug("==================================================================================================")
    

    where:
    tenantid | heirachy | expected | runthrough
    'TestTenantG' | ['dir1', 'dir2', 'dir3', 'dir1'] | 'fails because loop' | 1
    'TestTenantG' | ['dir4', 'dir5', 'dir6'] | 'succeeds' | 2
  }


  void "test external api"(String tenant_id, friend_url) {

    when: "We ask the externl API for the index of entries"
      setHeaders(['X-Okapi-Tenant': tenant_id])
      def resp = httpClient.get {
        request.uri = "$baseUrl/directory/externalApi/entry".toString()
        request.contentType = JSON[0]
        request.headers = (specDefaultHeaders + headersOverride + ['X-Okapi-Tenant': tenant_id])
      }

    then: "We get back the expected list"
      // dirent.name == name
      log.debug("externalApi list: ${resp}");
      resp != null

    where:
      tenant_id | friend_url
      'TestTenantG' | 'https://raw.githubusercontent.com/openlibraryenv'

  }

  void "test freshen worker thread"() {
    when: "We call the freshen endpoint"
      String tenant_id = 'TestTenantG'
      setHeaders(['X-Okapi-Tenant': tenant_id])
      def resp = httpClient.get {
        request.uri = "$baseUrl/directory/settings/foaf".toString()
        request.contentType = JSON[0]
        request.headers = (specDefaultHeaders + headersOverride + ['X-Okapi-Tenant': tenant_id])
      }

    then: "All is well"
      // dirent.name == name
      log.debug("externalApi list: ${resp}");
      resp != null

  }
}


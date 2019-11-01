package org.olf

import grails.testing.mixin.integration.Integration
import grails.transaction.*
import static grails.web.http.HttpHeaders.*
import static org.springframework.http.HttpStatus.*
import spock.lang.*
import geb.spock.*
import grails.plugins.rest.client.RestBuilder
import groovy.util.logging.Slf4j
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.k_int.okapi.OkapiHeaders
import spock.lang.Shared
import grails.gorm.multitenancy.Tenants
import org.olf.okapi.modules.directory.DirectoryEntry


@Slf4j
@Integration
@Stepwise
class DirectoryEntrySpec extends GebSpec {

  @Shared
  private Map test_info = [:]

  def grailsApplication

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

      logger.info("Post new tenant request for ${tenantid} to ${baseUrl}_/tenant");

      def resp = restBuilder().post("${baseUrl}_/tenant") {
        header 'X-Okapi-Tenant', tenantid
        authHeaders.rehydrate(delegate, owner, thisObject)()
      }

    then:"The response is correct"
      resp.status == CREATED.value()

    where:
      tenantid | name
      'TestTenantG' | 'TestTenantG'
  }

  void "test directory entry creation"(tenantid, name) {

    logger.info("Sleep 2s to see that schema creation went OK - running test for ${tenantid} ${name}");

    // Switching context, just want to make sure that the schema had time to finish initialising.
    Thread.sleep(2000)

    Map new_entry = [
          name:name,
          slug:name,
          description:'hello',
        ]


    when: "We create a new directory entry"
      def dirent = null;
      // Tenants.withId(tenantid.toLowerCase()+'_mod_directory') {
      //   DirectoryEntry.withTransaction {
      //     dirent = DirectoryEntry.findByName(name) ?: new DirectoryEntry(name:name, slug:name).save(flush:true, failOnError:true);
      //   }
      // }
      def resp = restBuilder().post("$baseUrl/directory/entry") {
        header 'X-Okapi-Tenant', tenantid
        authHeaders.rehydrate(delegate, owner, thisObject)()
        contentType 'application/json'
        accept 'application/json'
        json new_entry
      }

    then: "New directory entry created with the given name"
      // dirent.name == name
      resp.status == CREATED.value()


    where:
      tenantid | name
      'TestTenantG' | 'University of DIKU'
  }



  void "add Friend"(tenant_id, friend_url) {

    logger.info("Add a friend");

    when: "We add a new friend"
      def dirent = null;
      def resp = restBuilder().get("$baseUrl/directory/api/addFriend?friendUrl=$friend_url") {
        header 'X-Okapi-Tenant', tenant_id
        authHeaders.rehydrate(delegate, owner, thisObject)()
        accept 'application/json'
      }

    then: "New directory entry created with the given name"
      // dirent.name == name
      resp.status == OK.value()

    where:
      tenant_id | friend_url
      'TestTenantG' | 'https://raw.githubusercontent.com/openlibraryenvironment/mod-directory/master/seed_data/test/test_cons.json'
  }





  // Check parent loop failure
  void "Check Parent loop fails"(tenantid, heirachy, expected, runthrough) {
    logger.info("Checking parent loop");
    logger.debug("========================================== runthrough ${runthrough} ==========================================")
    
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
        dir1 = DirectoryEntry.findByName('dir1') ?: new DirectoryEntry(name:'dir1', slug:'dir1').save(flush:true, failOnError:true);
      }
    }
    Tenants.withId(tenantid.toLowerCase()+'_mod_directory') {
      DirectoryEntry.withTransaction {
        dir2 = DirectoryEntry.findByName('dir2') ?: new DirectoryEntry(name:'dir2', slug:'dir2').save(flush:true, failOnError:true);
      }
    }
    Tenants.withId(tenantid.toLowerCase()+'_mod_directory') {
      DirectoryEntry.withTransaction {
        dir3 = DirectoryEntry.findByName('dir3') ?: new DirectoryEntry(name:'dir3', slug:'dir3').save(flush:true, failOnError:true);
      }
    }
    Tenants.withId(tenantid.toLowerCase()+'_mod_directory') {
      DirectoryEntry.withTransaction {
        dir4 = DirectoryEntry.findByName('dir4') ?: new DirectoryEntry(name:'dir4', slug:'dir4').save(flush:true, failOnError:true);
      }
    }
    Tenants.withId(tenantid.toLowerCase()+'_mod_directory') {
      DirectoryEntry.withTransaction {
        dir5 = DirectoryEntry.findByName('dir5') ?: new DirectoryEntry(name:'dir5', slug:'dir5').save(flush:true, failOnError:true);
      }
    }
    Tenants.withId(tenantid.toLowerCase()+'_mod_directory') {
      DirectoryEntry.withTransaction {
        dir6 = DirectoryEntry.findByName('dir6') ?: new DirectoryEntry(name:'dir6', slug:'dir6').save(flush:true, failOnError:true);
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
      logger.debug("An error has occured: ${e}")
        parentValidation = 'fails because loop'
    }
    catch (Exception e) {
      logger.debug("An error has occured: ${e}")
      parentValidation = 'fails otherwise'
    }


    then: "Check that we get the expected validation failures"
    logger.debug("Checking validator. Expected: ${expected}, Validation: ${parentValidation}")
    expected == parentValidation

    logger.debug("==================================================================================================")
    

    where:
    tenantid | heirachy | expected | runthrough
    'TestTenantG' | ['dir1', 'dir2', 'dir3', 'dir1'] | 'fails because loop' | 1
    'TestTenantG' | ['dir4', 'dir5', 'dir6'] | 'succeeds' | 2
  }




  void "Delete the tenants"(tenant_id, note) {

    logger.info("Delete test friend");

    expect:"post delete request to the OKAPI controller for "+tenant_id+" results in OK and deleted tennant"
      def resp = restBuilder().delete("$baseUrl/_/tenant") {
        header 'X-Okapi-Tenant', tenant_id
        authHeaders.rehydrate(delegate, owner, thisObject)()
      }

      logger.debug("completed DELETE request on ${tenant_id}");
      resp.status == NO_CONTENT.value()

    where:
      tenant_id | note
      'TestTenantG' | 'note'
  }

  RestBuilder restBuilder() {
    new RestBuilder()
  }

}


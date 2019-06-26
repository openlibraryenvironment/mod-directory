package com.k_int.refdata

import org.apache.commons.lang.math.RandomUtils
import org.olf.okapi.modules.directory.Service

import com.k_int.okapi.OkapiHeaders
import com.k_int.okapi.OkapiTenantResolver
import com.k_int.web.toolkit.refdata.RefdataValue
import com.k_int.web.toolkit.testing.HttpSpec
import grails.gorm.multitenancy.Tenants
import grails.testing.mixin.integration.Integration
import groovy.util.logging.Slf4j
import spock.lang.Shared
import spock.lang.Stepwise

@Slf4j
@Integration
@Stepwise
class RefdataSpec extends HttpSpec {
  def setupSpec() {
    addDefaultHeaders(
      (OkapiHeaders.TENANT): 'http_tests',
      (OkapiHeaders.USER_ID): 'http_test_user'
    )
    
    setHttpClientConfig {
      client.clientCustomizer { HttpURLConnection conn ->
        conn.connectTimeout = 10000
        conn.readTimeout = 5000
      }
    }
  }
  
  def cleanupSpecWithSpring() {
    Map resp = doDelete('/_/tenant', null)
  }
  
  void 'Ensure test tenant' () {
    given: 'Post to tenant API'
      def resp = doPost('/_/tenant', {
        parameters ([
          {
            key 'loadReference'
            value (true)
          }
        ])
      })
      
      // Nasty... Would like a waitFor on the events. But for now this will do.
      Thread.sleep(4000)
    
    expect: 'Response'
      resp != null
  }
  
  static final List<Map> CATEGORIES = [[
      desc: 'TestCat1',
      values: [
        [ label: "Red" ],
        [ label: "Blue" ],
        [ label: "Yellow" ],
        [ label: "Cyan" ]
      ]
    ]]

  @Shared Serializable first_cat_id
  @Shared int value_count
  
  void "Create Test Categories" () {
    given: 'Fetch current categories'
      Map httpResult = doGet('/directory/refdata', ['stats': true])
      final int initial_cats = httpResult.total
      
    and: 'Add all categories'
      for (int i=0; i<CATEGORIES.size();i++ ) {
        Map cat_def = CATEGORIES[i]
        httpResult = doPost('/directory/refdata', cat_def)        
        if (i == 0) {
          first_cat_id = httpResult.id
          value_count = httpResult['values'].size()
        }
      }
      
      // Refetch
    and: 'Re-Fetch category list'
      httpResult = doGet('/directory/refdata', ['stats': true])
      
    expect: 'Categories count increased'
      assert httpResult.total == (initial_cats + CATEGORIES.size())
  }

  
  void "Test refdata manipulation" () {
    when: 'Adding a new value to the category'
      final String label_to_add = 'Black'
      
      Map httpResult = doPut("/directory/refdata/${first_cat_id}", {
        values ([{ label (label_to_add) }])
      })
      
    and: 'Refetch'
      httpResult = doGet("/directory/refdata/${first_cat_id}")
      
    then: 'Extra value added'
      assert httpResult.values.size() == value_count + 1
      assert (httpResult.values.find { it.label == label_to_add }) != null
      
    /* Originally @Link https://openlibraryenvironment.atlassian.net/browse/PR-191 */
    when: 'Value requested to be deleted [PR-191]'
      final String value_id_to_delete = httpResult.values[0].id
      
      httpResult = doPut("/directory/refdata/${first_cat_id}", {
        values ([{
          id (value_id_to_delete)
          '_delete' (true)
        }])
      })
      
    then: 'Value removed from list'
      assert httpResult.values.size() == value_count
      assert (httpResult.values.find { it.id == value_id_to_delete }) == null
      
    /* Originally @Link https://openlibraryenvironment.atlassian.net/browse/PR-190 */
    when: 'Modifying an existing value [PR-190]'
      final String value_id_to_modify = httpResult.values[0].id
      final String new_label = 'Unknowable'
      
      httpResult = doPut("/directory/refdata/${first_cat_id}", {
        values ([{
          id (value_id_to_modify)
          label (new_label)
        }])
      })
      
    then: 'Value label modified and no new items'
      assert httpResult.values.size() == value_count
      assert (httpResult.values.find { it.id == value_id_to_modify && it.label == new_label}) != null
  }
  
  void "Set refdata in code" () {
    
    Tenants.withId(OkapiTenantResolver.getTenantSchemaName('http_tests')) {
      when: 'Create and fetch necessary data'
        // Get all allowed values for the type field.
        final Set<RefdataValue> vals = Service.allTypeValues()
        
        // Create a service first.
        Service service = new Service (address: 'Testing address', name: 'Test Sevrice')
        
      and: 'Set the type directly'
        
        // Random value.
        RefdataValue val = vals[ RandomUtils.nextInt(vals.size()) ]
        
        service.type = val
        service.save(failOnError: true)
        service.refresh()
        
      then: 'Values match'
        assert service.type.id == val.id
        
      when: 'Set exisitng value via the from string method'
        val = vals[ RandomUtils.nextInt(vals.size()) ]
        
        service.setTypeFromString (val.value)
        service.save(failOnError: true)
        service.refresh()
        
      then: 'Values match'
        assert service.type.id == val.id
    }
  }
  
  void "Set refdata via web api" () {
    Set<RefdataValue> vals
    when: 'Grab the service type values'
      // Get all allowed values for the type field straight from the database.
      Tenants.withId(OkapiTenantResolver.getTenantSchemaName('http_tests')) {
        vals = Service.allTypeValues()
      }
      
    and: 'Add new via the API using the id'
      RefdataValue val = vals[ RandomUtils.nextInt(vals.size()) ]
      def resp = doPost('/directory/service', {
        name 'Test Service 2'
        address 'Test addres 2'
        
        // Set random value.
        type (val.id)
      })
      
      String created_id = resp.id
      
    and: 'Refetch'
      resp = doGet("/directory/service/${created_id}")
    
    then: 'Values match'
      resp.type.id == val.id
      
    when: 'Add new via the API using the value'
      val = vals[ RandomUtils.nextInt(vals.size()) ]
      resp = doPost('/directory/service', {
        name 'Test Service 3'
        address 'Test addres 3'
        
        // Set random value.
        type (val.value)
      })
      
      created_id = resp.id
      
    and: 'Refetch'
      resp = doGet("/directory/service/${created_id}")
    
    then: 'Values match'
      resp.type.id == val.id
      
    when: 'Update via the API using the id'
      val = vals[ RandomUtils.nextInt(vals.size()) ]
      resp = doPut("/directory/service/${created_id}", {
        // Set random value.
        type (val.id)
      })
      
    and: 'Refetch'
      resp = doGet("/directory/service/${created_id}")
    
    then: 'Values match'
      resp.type.id == val.id
      
    when: 'Update via the API using the value'
      val = vals[ RandomUtils.nextInt(vals.size()) ]
      resp = doPut("/directory/service/${created_id}", {
        // Set random value.
        type (val.value)
      })
      
    and: 'Refetch'
      resp = doGet("/directory/service/${created_id}")
    
    then: 'Values match'
      resp.type.id == val.id
    
  }
}

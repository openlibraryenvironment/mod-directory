package org.olf

import com.k_int.okapi.OkapiHeaders
import com.k_int.web.toolkit.testing.HttpSpec

import groovyx.net.http.HttpException
import spock.lang.Stepwise
import spock.util.concurrent.PollingConditions

@Stepwise
abstract class BaseSpec extends HttpSpec {

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

  Map<String, String> getAllHeaders() {
    specDefaultHeaders + headersOverride
  }
  
  String getCurrentTenant() {
    allHeaders?.get(OkapiHeaders.TENANT)
  }
  
  void 'Pre purge tenant' () {
    boolean resp = false
    when: 'Purge the tenant'
      try {
        resp = doDelete('/_/tenant', null)
        resp = true
      } catch (HttpException ex) { resp = true }
      
    then: 'Response obtained'
      resp == true
  }
  
  void 'Ensure test tenant' () {
    
    when: 'Create the tenant'
      def resp = doPost('/_/tenant', {
      parameters ([["key": "loadReference", "value": true]])
    })

    then: 'Response obtained'
    resp != null

    and: 'Refdata added'

      List list
      // Wait for the refdata to be loaded.
      def conditions = new PollingConditions(timeout: 10)
      conditions.eventually {
        (list = doGet('/erm/refdata')).size() > 0
      }
  }
}

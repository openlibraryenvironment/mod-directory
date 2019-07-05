import grails.gorm.multitenancy.Tenants
import grails.events.annotation.Subscriber
import grails.gorm.multitenancy.WithoutTenant
import grails.gorm.transactions.Transactional

import org.olf.okapi.modules.directory.Service
import com.k_int.web.toolkit.refdata.RefdataValue
import com.k_int.web.toolkit.refdata.RefdataCategory
import com.k_int.web.toolkit.custprops.CustomPropertyDefinition
import com.k_int.web.toolkit.custprops.types.CustomPropertyText;

import org.olf.okapi.modules.directory.NamingAuthority;

import grails.databinding.SimpleMapDataBindingSource



CustomPropertyDefinition ensureTextProperty(String name) {
  CustomPropertyDefinition result = CustomPropertyDefinition.findByName(name) ?: new CustomPropertyDefinition(
                                        name:name,
                                        type:com.k_int.web.toolkit.custprops.types.CustomPropertyText.class
                                      ).save(flush:true, failOnError:true);
  return result;
}

Service ensureService(String name, String type, List<String>tags, String address, Map custProps) {

  def result = Service.findByName(name);

  if ( result == null ) {

    // Until we figure out a good way to inject the databinder, lets do this the old way
    result = new org.olf.okapi.modules.directory.Service(
      name: name,
      type: RefdataValue.lookupOrCreate('Service.Type', 'ISO18626'),
      address: address
    )

    // This is an experiement - lets call the databinder so we can exploit all the clever functions that REST clients 
    // use like the auto-lookup of refdata and the resolution of custprops property names.
    //result = new org.olf.okapi.modules.directory.Service()
    // Map service_props = [
    //   name: name,
    //   type: type,
    //   address: address,
    //   tags: tags,
    //   customProperties: custProps
    // ]
    // grailsWebDataBinder.bind result, service_props as SimpleMapDataBindingSource
    result.save(flush:true, failOnError:true);
  }

  return result;
}

log.info 'Importing sample data'

if (existing_tenant) {
  log.info 'Skipping exisiting tenant'
  return
}

try {
  log.debug("Sleeping.....")
  synchronized(this) {
    Thread.sleep(3000);
  }
}
catch ( Exception e ) {
}

def cp_ns = ensureTextProperty('ILLPreferredNamespaces');
def cp_url = ensureTextProperty('url');
def cp_demoprop = ensureTextProperty('demoCustprop');

def iso_18626_loopback_service = ensureService('loopback-iso-18626',
                                                       'ISO18626',
                                                       ['system-default'],
                                                       'http://localhost:9130/rs/iso18626',
                                                       null);

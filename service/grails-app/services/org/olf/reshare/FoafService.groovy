package org.olf.reshare

import grails.gorm.transactions.Transactional
import org.olf.okapi.modules.directory.DirectoryEntry
import groovyx.net.http.HTTPBuilder
import groovyx.net.http.Method
import groovyx.net.http.ContentType

/**
 *
 */
@Transactional
class FoafService {

  private static long MIN_READ_INTERVAL = 60 * 60 * 24 * 7 * 1000; // 7 days between directory reads

  /**
   * Add the identified url as a friend
   */
  public void addFriend(String url) {

    log.debug("addFriend(${url})");

    if ( ( url != null ) &&
         ( url.length() > 5 ) &&
         ( url.toLowerCase().startsWith('http') ) ) {


      if ( shouldVisit(url) ) {
        log.debug("Visiting....");
        processFriend(url);
      }
    }
  }

  // return true if we should attempt to visit this URL - currently
  // returns false if we already know about this URL and have not recently visited it
  private boolean shouldVisit(String url) {
    boolean result = false;
    DirectoryEntry de = DirectoryEntry.findByFoafUrl(url)
    if ( de != null ) {
      if ( ( de.foafTimestamp == null ) ||
           ( System.currentTimeMillis() - de.foafTimestamp > MIN_READ_INTERVAL ) ) {
        // We know this FOAF URL before but it has never been visited, or it 
        // was more than MIN_READ_INTERVAL ms ago, so lets reread.
        result = true;
      }
    }
    else {
      // We have not seen this URL before (At some point, we should probably have a blacklist to check)
      result = true;
    }

    return result;
  }

  private void processFriend(String url) {
    def http = new HTTPBuilder(url)
    //http.auth.basic ('username','password')
    http.request(Method.GET, ContentType.JSON) {
      headers.'Content-Type' = 'application/json'
      response.success = { resp, json ->
        log.debug("Got json response ${json}");
        // Make sure that the JSON really is an array of foaf descriptions
        if ( validateJson(json) ) {
          DirectoryEntry de = DirectoryEntry.findByFoafUrl(url) ?: new DirectoryEntry(foaf:url)
          de.foafTimestamp = System.currentTimeMillis();
          updateFromJson(de,json)
          de.save(flush:true, failOnError:true);
        }
      }
      response.failure = { json ->
        log.warn("Problem processing FOAF URL ${url}");
      }
    }
  }

  private boolean validateJson(json) {
    boolean result = true;
    return result;
  }

  // return true if record updated
  private boolean updateFromJson(DirectoryEntry de, Map json) {
    boolean result = false

    // do update
    result &= mergeField('foaf', 'foafUrl', de, json);
    result &= mergeField('name', 'name', de, json);
    // result &= mergeField('url', 'url', de, json);
    result &= mergeField('description', 'description', de, json);
    result &= mergeField('slug', 'slug', de, json);

    return result;
  }

  /**
   * Check to see if fieldname is present in json, and if so, compare it to the current value of that field in the object
   * if different, set that field on the object and return true to signify that the record was updated.
   */
  private boolean mergeField(String jsonField, String domainModelField, Object obj, Map json) {
    boolean result = false;
    if ( json[jsonField] != null ) {
      obj[domainModelField] = json[jsonField]
      result = true;
    }
    return result;
  }
}

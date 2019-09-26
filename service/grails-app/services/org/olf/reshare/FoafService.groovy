package org.olf.reshare

import org.olf.okapi.modules.directory.DirectoryEntry

import grails.events.annotation.Subscriber
import grails.gorm.multitenancy.Tenants
import grails.gorm.transactions.Transactional
import grails.web.databinding.DataBinder
import groovyx.net.http.ContentType
import groovyx.net.http.HTTPBuilder
import groovyx.net.http.Method
import org.hibernate.StatelessSession;
import org.hibernate.Transaction;


/**
 *
 */
class FoafService implements DataBinder {

  def sessionFactory

  private static long MIN_READ_INTERVAL = 60 * 60 * 24 * 7 * 1000; // 7 days between directory reads

  // This is important! without it, all updates will be batched inside a single transaction and
  // we don't want that.
  static transactional = false;

  


  @Subscriber('okapi:dataload:sample')
  public void afterSampleLoaded (final String tenantId, final String value, final boolean existing_tenant, final boolean upgrading, final String toVersion, final String fromVersion) {
    log.debug("Sleep until load");
    try {
      Thread.sleep(1000*20);
    }
    catch ( Exception e ) {
    }

    log.debug("Process....");
    // See if we can find the URL of our seed entry in the directory
    Tenants.withId(tenantId+'_mod_directory') {
      checkFriend('https://raw.githubusercontent.com/openlibraryenvironment/mod-directory/master/seed_data/olf.json');
    }
  }


  /**
   * Check a URL to see if it needs to be visited.
   */
  public void checkFriend(String url, int depth = 0) {

    log.debug("checkFriend(${url}, ${depth})");

    if ( ( url != null ) &&
         ( url.length() > 5 ) &&
         ( url.toLowerCase().startsWith('http') ) && 
         ( depth < 4 ) ) {
      if ( shouldVisit(url) ) {
        log.debug("Visiting....${url}");
        processFriend(url, depth);
      }
    }
  }

  // return true if we should attempt to visit this URL - currently
  // returns false if we already know about this URL and have recently visited it
  private boolean shouldVisit(String url) {
    boolean result = false;
    int p = DirectoryEntry.executeQuery('select count(de.id) from DirectoryEntry as de where de.foafUrl=:url and ( ( de.foafTimestamp + :min_interval ) < :now )',
                                                    [url:url, min_interval:MIN_READ_INTERVAL, now: System.currentTimeMillis()])[0];
    if ( p == 0 ) {
      // We know this FOAF URL before but it has never been visited, or it 
      // was more than MIN_READ_INTERVAL ms ago, so lets reread.
      log.debug("No directory entry found for foaf URL ${url} and timestamp expired");
      result = true;
    }
    else {
      // We have not seen this URL before (At some point, we should probably have a blacklist to check)
      log.debug("Matched a directory entry for foaf URL ${url} but its not expired yet")
      result = false;
    }

    return result;
  }

  private void processFriend(String url, int depth=0) {
    try {
      def http = new HTTPBuilder(url)
      //http.auth.basic ('username','password')
      http.request(Method.GET, ContentType.JSON) {
        headers.'Content-Type' = 'application/json'
        response.success = { resp, json ->
          log.debug("Got json response... slug is ${json?.slug}");

          // Make sure that the JSON really is an array of foaf descriptions
          if ( validateJson(json) ) {

            // Remove friends before processing
            Object friends_list = json.remove('friends')

            // Remove the friends list - we will process it later on
            Object announcements = json.remove('announcements')

            // StatelessSession session = sessionFactory.openStatelessSession();
            // Transaction tx = session.beginTransaction();

            DirectoryEntry.withSession { session ->
              session.clear();
              DirectoryEntry.withTransaction { status ->
                log.info("processing directory entry ${url} - see if we have an entry for that URL or slug ${json.slug}\n\n");
  
                // Look up the directory entry for the root
                DirectoryEntry de = DirectoryEntry.findByFoafUrlOrSlug(url, json.slug)
  
  
                log.debug("Result of DirectoryEntry.findByFoafUrlOrSlug(${url},${json.slug}) : ${de?.id}(${de?.version})")
  
                if ( de == null ) {
                  log.debug("Create a new directory entry(foafUrl:${url}, name:${json.name})")
                  de = new DirectoryEntry(foafUrl:url, name: json.name)
                }
                else {
                  log.debug("DE already exists - lock and refresh (${de.id},${de.version})");
                  def iqr = DirectoryEntry.executeQuery('select de.id, de.version from DirectoryEntry as de where de.id=:id',[id:de.id]);
                  log.debug("Query version: ${iqr}");
                  de.lock()
                }
    
                // Load the json over the domain object
                log.debug("About to call doBind(${de},${json})")
                log.debug("Info about de.addresses: ${de.addresses} ${de.addresses?.size()} ${de.addresses?.class?.name}");
                
                bindData (de, json)

                if ( ( de.foafUrl == null ) && ( url != null ) )
                  de.foafUrl = url;
    
                // update the touched timestamp
                de.foafTimestamp = System.currentTimeMillis();
    
                log.debug("Dumping DE befoe save.....");
                dumpDE(de);

                // save
                log.debug("Saving...");
                de.save(flush:true, failOnError:true);
                session.flush();
              }
            }

            // tx.commit();
            // session.close();
  
            // Process any friends
            log.debug("Processing friends ${friends_list}");
            if ( friends_list ) {
              friends_list.each { fr ->
                if ( fr.foaf ) {
                  checkFriend(fr.foaf, depth+1);
                }
              }
            }
          }
        }
        response.failure = { json ->
          log.warn("Problem processing FOAF URL ${url}");
        }
      }
    }
    catch ( Exception e ) {
      log.error("Problem trying to process FOAF URL ${url} : ${e.message}",e)
    }
    finally {
      log.debug("leaving processFriend(${url},${depth}");
    }
  }

  private boolean validateJson(json) {
    boolean result = false;
    if ( ( json.name != null ) &&
         ( json.name.length() > 0 ) ) {
      result = true;
    }
    return result;
  }

  // return true if record updated
  private boolean updateFromJson(DirectoryEntry de, Map json) {
    boolean result = false

    // do update
    result &= mergeField('name', 'name', de, json);
    // result &= mergeField('url', 'url', de, json);
    result &= mergeField('description', 'description', de, json);

    // Normally immutable
    // result &= mergeField('slug', 'slug', de, json);

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

  private void dumpDE(DirectoryEntry de, depth=0) {
    if ( de != null ) {
      log.debug("${'  '*depth} - DE ${de.id} ${de.version} ${de.slug}");
      log.debug("${'  '*depth} - Symbols array: ${de.symbols?.class?.name} ${de.symbols?.size()}");
      de.symbols.each {
        log.debug("${'  '*depth}   - SYMBOL ${it.id} ${it}");
      }
      de.units.each {
        dumpDE(it, depth+1);
      }
    }
  }
}

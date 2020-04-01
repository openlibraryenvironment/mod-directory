package org.olf.reshare

import org.olf.okapi.modules.directory.DirectoryEntry
import org.olf.okapi.modules.directory.GroupMember

import grails.events.annotation.Subscriber
import grails.gorm.multitenancy.Tenants
import grails.gorm.transactions.Transactional
import grails.web.databinding.DataBinder
import groovyx.net.http.ContentType
import groovyx.net.http.HTTPBuilder
import groovyx.net.http.Method
import org.hibernate.StatelessSession;
import org.hibernate.Transaction;
import grails.async.Promise
import static grails.async.Promises.*


/**
 *
 */
class FoafService implements DataBinder {

  def sessionFactory

  private static long MIN_READ_INTERVAL = 60 * 60 * 24 * 2 * 1000; // 2 days between directory reads

  // This is important! without it, all updates will be batched inside a single transaction and
  // we don't want that.
  static transactional = false;

  private static String GROUP_MEMBER_QUERY = '''select gm.id
from GroupMember as gm
where gm.groupOrg.slug=:group 
and gm.memberOrg.slug=:member
'''

  


  @Subscriber('okapi:dataload:sample')
  public void afterSampleLoaded (final String tenantId, final String value, final boolean existing_tenant, final boolean upgrading, final String toVersion, final String fromVersion) {
    log.debug("FoafService::afterSampleLoaded");
    // See if we can find the URL of our seed entry in the directory
    Tenants.withId(tenantId+'_mod_directory') {
      checkFriend('https://raw.githubusercontent.com/openlibraryenvironment/mod-directory/master/seed_data/olf.json');
    }
    log.debug("FoafService::afterSampleLoaded COMPLETE");
  }


  /**
   * Check a URL to see if it needs to be visited.
   */
  public void checkFriend(String url, int depth = 0) {
    if ( ( url != null ) &&
         ( url.length() > 5 ) &&
         ( url.toLowerCase().startsWith('http') ) && 
         ( depth < 4 ) ) {
      if ( shouldVisit(url) ) {
        processFriend(url, depth);
      }
    }
  }

  // return true if we should attempt to visit this URL - currently
  // returns false if we already know about this URL and have recently visited it
  private boolean shouldVisit(String url) {
    boolean result = false;

    // Count all the directory entries for url=X and cache still valid
    int p = DirectoryEntry.executeQuery('select count(de.id) from DirectoryEntry as de where de.foafUrl=:url and ( :now < ( de.foafTimestamp + :min_interval ) )',
                                                    [url:url, min_interval:MIN_READ_INTERVAL, now: System.currentTimeMillis()])[0];
    if ( p == 0 ) {
      // unable to find that URL OR the cache has expired, so we should re-visit
      result = true;
    }
    else {
      // We found that URL and it's cache data is still within MIN_READ_INTERVAL, so we don't need to check
      result = false;
    }

    log.debug("shouldVisit(${url}) : ${result}(${p})");

    return result;
  }

  private void processFriend(String url, int depth=0) {
    log.debug("processFriend(${url},${depth})");
    try {
      def http = new HTTPBuilder(url)

      log.debug("HTTP GET ${url}");
      //http.auth.basic ('username','password')
      http.request(Method.GET, ContentType.JSON) {
        headers.'Content-Type' = 'application/json'
        response.success = { resp, json ->
          log.debug("HTTP get ${url} :: Success");
          // Make sure that the JSON really is an array of foaf descriptions
          if ( validateJson(json) ) {

            Object addresses_list = json.remove('addresses')

            // Remove friends before processing
            Object friends_list = json.remove('friends')
            Object member_list = json.remove('members')

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
                  String new_id = java.util.UUID.randomUUID().toString();
                  log.debug("Create a new directory entry(id:${new_id},foafUrl:${url}, name:${json.name})")
                  de = new DirectoryEntry( foafUrl:url, name: json.name)
                  de.id=new_id;
                }
                else {
                  log.debug("DE already exists - lock and refresh (${de.id},${de.version})");
                  // def iqr = DirectoryEntry.executeQuery('select de.id, de.version from DirectoryEntry as de where de.id=:id',[id:de.id]);
                  // log.debug("Query version: ${iqr}");
                  de.lock()
                }
    
                log.debug("bind json ${json}");
                bindData (de, json)

                if ( ( de.foafUrl == null ) && ( url != null ) )
                  de.foafUrl = url;
    
                // update the touched timestamp
                de.foafTimestamp = System.currentTimeMillis();
    
                // dumpDE(de);

                // save
                log.debug("Save de - ID is ${de.id}");
                de.save(flush:true, failOnError:true);
                session.flush();
              }
            }

            // tx.commit();
            // session.close();
  
            // Process any friends
            log.debug("Processing friends list from entry at (${url}): ${friends_list}");
            if ( friends_list != null ) {
              friends_list.each { fr ->
                if ( fr.foaf ) {
                  checkFriend(fr.foaf, depth+1);
                }
              }
            }

            try {
              if ( member_list != null ) {
                log.debug("Process members");
                boolean save_needed = false;
                
                // After we have processed all friend links lets make any links we need to
                DirectoryEntry.withSession { session ->
                  session.clear();
                  DirectoryEntry.withTransaction { status ->
  
                    DirectoryEntry owner = DirectoryEntry.findBySlug(json.slug);
  
                    if ( owner != null ) {
                      member_list.each { mem ->
                        log.debug("Checking member ${mem}");
                        List<GroupMember> gm_list = GroupMember.executeQuery(GROUP_MEMBER_QUERY, [group:json.slug, member: mem.memberOrg])
                        if ( gm_list.size() == 0 ) {
                          DirectoryEntry member_org = DirectoryEntry.findBySlug(mem.memberOrg);
                          if ( member_org != null ) {
                            def gm = new GroupMember( groupOrg: owner, memberOrg: member_org).save(flush:true, failOnError:true);
                          }
                          else {
                            log.error("unexpected missing member org : ${mem.memberOrg} for parent ${json.slug}");
                          }
                        }
                      }
                    }
                    else {
                      log.error("Owner slug ${json.slug} not found");
                    }
                  }
                }
              }
            }
            catch ( Exception e ) {
              log.error("Problem processing member list",e);
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


  public freshen(String tenant) {
    log.debug("FoafService::freshen(${tenant})");
    Promise p = task {
      Tenants.withId(tenant+'_mod_directory') {
        DirectoryEntry.withNewSession {
          DirectoryEntry.executeQuery('select de.foafUrl, de.foafTimestamp from DirectoryEntry as de where de.foafUrl is not null').each { row ->
            def foaf_url = row[0]
            def foaf_ts = row[1]

            log.debug("freshen() checking ${foaf_url} last:${foaf_ts} remaining:${(MIN_READ_INTERVAL - ( System.currentTimeMillis() - foaf_ts?:0 ))/(60000) }mins");
            checkFriend(foaf_url);
          }
        }
      }
    }

    p.onError { Throwable err ->
      log.error("Problem",err);
    }

    p.onComplete { result ->
      log.debug("this.freshen promise complete");
    }


  }
}

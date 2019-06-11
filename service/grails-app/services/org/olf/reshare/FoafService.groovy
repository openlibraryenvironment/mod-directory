package org.olf.reshare

import grails.gorm.transactions.Transactional
import org.olf.okapi.modules.directory.DirectoryEntry

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
         ( url.length > 5 ) &&
         ( url.startsWith('https') ) ) {


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
      if ( ( de.lastFoafReadTimestamp == null ) ||
           ( System.currentTimeMillis() - de.lastFoafReadTimestamp > MIN_READ_INTERVAL ) ) {
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
  }
}

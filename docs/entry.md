# DirectoryEntry Resources

This document is a guid to DirectoryEntry resources. Examples are provided, but the authoritative source
should be the source code. DirectoryEntry domain instances are defined by the [Directory Entry domain class](https://github.com/openlibraryenvironment/mod-directory/blob/master/service/grails-app/domain/org/olf/okapi/modules/directory/DirectoryEntry.groovy) and the particular JSON format returned is under the control of [the gson template for directoryEntry](https://github.com/openlibraryenvironment/mod-directory/blob/master/service/grails-app/views/entry/_entry.gson)

## Creating a new DirectoryEntry

Posting JSON to /directory/entry will create a new directory entry. The following example is from [test2.sh](https://github.com/openlibraryenvironment/mod-directory/blob/master/scripts/test2.sh)

    diku_entry_rec=`curl -sSL -H "X-Okapi-Tenant: diku" -H "X-Okapi-Token: $AUTH_TOKEN" -H "Content-Type: application/json" -X POST http://localhost:9130/directory/entry -d ' {
      "name":"diku",
      "slug":"diku",
      "description":"diku",
      "tags":[
        "Testdata"
      ],
      "customProperties": {
        "url": [ "http://some.url" ]
      }
    }'`

## Retrive a DirectoryEntry

Issue a HTTP-GET to /directory/entry/{UUID} to retrieve a specific entry

## Updating an existing DirectoryEntry

## List DirectoryEntry records

## List DirectoryEntry records with filters

## Search DirectoryEntry records with text matching

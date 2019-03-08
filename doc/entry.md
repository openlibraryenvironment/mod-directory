# DirectoryEntry Resources

<!-- md2toc -l 2 entry.md -->
* [Creating a new DirectoryEntry](#creating-a-new-directoryentry)
* [Retrive a DirectoryEntry](#retrive-a-directoryentry)
* [Updating an existing DirectoryEntry](#updating-an-existing-directoryentry)
* [Searching and filtering DirectoryEntry records](#searching-and-filtering-directoryentry-records)


This document is a guid to DirectoryEntry resources. Examples are provided, but the authoritative source
should be the source code. DirectoryEntry domain instances are defined by the [Directory Entry domain class](../service/grails-app/domain/org/olf/okapi/modules/directory/DirectoryEntry.groovy) and the particular JSON format returned is under the control of [the gson template for directoryEntry](../service/grails-app/views/entry/_entry.gson)

## Creating a new DirectoryEntry

Posting JSON to /directory/entry will create a new directory entry. The following example is from [test2.sh](../scripts/test2.sh)

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

XXX to do

## Searching and filtering DirectoryEntry records

In a GET request to `/directory/entry`, you can provide the following URL query parameters:

* `term` -- a datum to search for using free-text matching: for example, a word in the directory-entry's name. This may not be repeated.
* `match` -- the name of a non-calculated (i.e. physical) field in the directory-entry record-type, in which to seek the tern: for example, `name` or `tags.value`. (Here, `tags` is an array of objects; specifying `match=tags.value` finds matches in any of the `tags[n].value` fields.)
* `filters` -- an expression used for exact full-field matching: it is a `||`-separated list of one or more expressions, each of the form _field_ _op_ _value_, e.g. `tags.value==Branch`, the list being satisfied when any of its expressions is true. May be repeated, meaning that _all_ the `filters` expressions must be satisfied.
* `perPage` or `max` (synonymous) -- specifies the number of records to include in the response, enabling paging.
* `offset` -- specifies how many records to skip from the result-set (starting at, and defaulting to, zero).
* `page` -- an alternative to `offset` specifying which page of results to return (starting from 1). So `perpage=N&page=M` is equivalent to `perpage=N&offset=O` where O = (N-1)*M.
* `stats` -- if omitted, the resulting JSON consists only of a record list. If it's included and set to `true`, the result is a structure in which `records` contains the record-list, and additional information is also included at the top level: `pageSize`, `page`, `totalPages`, `meta`, `totalRecords` and (perhaps redundantly?) `total`.

The operators supported within a filter expression are:

* `=` or `==` for equality.
* `!=` or `<>` for inequality.
* `>`, `>=`, `<` and `<=` for ordering.
* `=i=` for case-insensitive equality. (All filtering is otherwise case-sensitive.)

Filter expressions may also take a special two-operator form _value1_ _op1_ _field_ _op2_ _value2_ for orderable fields such as numbers and dates, specifying a range: for example `32<=myClass.myPorperty<75` checks for `myClass.myProperty` greater or equal to 32 but strictly less than 75.

Some example queries:

* `/directory/entry?match=name&match=tags.value&term=college`
  -- finds records that have the term "college" in either the `name` or `tags.value` field.
* `/directory/entry?filters=tags.value==Branch||tags.value==Community`
  -- finds records in which at least one tag had the value either "Branch" or "Comunity".
* `/directory/entry?filters=foo=chicken||for=badger&filters=bar=weasel||bar=stoat` -- finds records where `foo` is either "chicken" or "badger", and `bar` is either "weasel" or "stoat". (There is no way to specify (A and B) or (C and D).)

(I _think_ these parameters are standard in the framework used for building most of the ReShare back-end modules, so similarly formed queries should work on other endpoints within mod-directory, and also in mod-rs and elsewhere.)

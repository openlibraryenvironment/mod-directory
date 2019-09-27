Bootstrapping data for mod-directory

# Introduction and rationale

Mod-Directory supports a mechanism for institutions to maintain and publish their own "Directory Entry". This distributes the "source of truth" for the directory data, and prevents any one player from taking ownership of the description of the interlending network. Because each node is responsible for their data, and that data is published in a simple to use format, the mechanism is amemnable to a number of different management approaches. Initially, we are hand-crafting these files, but it is very probably that mod-directory will provide a way to publish our "home" entry.

# SLUGs

In order to be able to properly update certain aspects of a directory entry it is necessary to use globally unique identifiers. We have chosen to adopt the "SLUG" concept from blogging - Maintainers of directory entries are asked to come up with human readable, but globally unique, identifiers for certain chunks of information. These identifiers ensure that when critical entries in the directory are updated, the right fields are updated in the right place. It is entirely permissable to reuse existing mnemonic identifiers - some organisations may wish to use their OCLC symbols for example. Others may wish to come up with a mnemonic that suits them best.

Currently, there are two crucial places in a directory where SLUGs are used:

1) The directory entry itself - Used to coordinate between the institution, and consortia who wish to maintain lists of members
2) At the level of a service being used by the institution - The slug is used to uniquely identify the instiututions use of a service. This is necessary as some institutions may make use of a shared service and we need a way to uniquely identify  the intersection of an institution and a shared service.

# Example Entries

There are several working examples of directory entries here: https://github.com/openlibraryenvironment/mod-directory/blob/master/seed_data 

# The properties

*IMPORTANT* Directory entries are recursive. Within an entry, the "entries" array can be used to list DirectoryEntries that are children of the parent. In this way, Institutions, Campus, Building, Department, Areas can be arbitrarily modelled. Each of the properties below can be used at any recursive level. It will be normal to specify services which are shared over all organisational units at the top level to denote that they are available to any child units. it is advisable to use a Tag to denote the level of the entry.

## name

The name of the institution or consortium

## slug

A globally unique but human readable identifier for the institution or consortium

## url

The institutional home page, or other URL you wish to associate with the directory entry

## tags

Any tags you wish to use in relation to the directory entry

## symbols

The symbols by which this unit are known - with an authority and a symbol, eg OCLC:ZMU

## units

Any child directory entries that this entry has

## services

Any services this entry makes available - EG RTAC, ILL, CIRC service endpoints.

## friends (Top level only)

A list of other insitutions this institution knows about

## announcements

*Not currently used* but a way to make announcements about service outage and non-availability of normal services.

# Walking the graph

When activated, a FOLIO mod-directory is asked to visit the OLF directory entry. At least in the initial phases of Project Re:Share we are relying upon the OLF to hold a list of the consortia
that make up the Re:Share network. initially this file contains the PALCI directory entry, which itself lists the members of that network.

At startup, mod-directory will walk the graph of friend links, loading each directory entry and keeping track of the time of last visit. THe maximum depth of this tree on any one run is 3.


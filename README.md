# mod-directory

## Purpose

Mod-Directory is an okapi service for use in resource sharing environments. It's purpose is to provide a bounded context for the storage, retrieval and update
of directory service information which underpins inter-library services. This directory contains, but is not restricted to

* Organisations and Organisational Units
* Service Endpoints within organisations
* Policy and Group Memberships (Consortia)

Mod-Directory acts as a means for an institution to maintain and publish it's own directory information, and as a repository for this information about remote or external
organisations, services and policies. Mod-Directory is the definitive local cache of partner institution information. If an application needs to know the preferred 
protocol for issuing an ILL request to a peer institution with whom we have a reciprocal lending agreement, mod-directory is responsible for answering that question.


## Resources and Contexts.

It's early days for mod-directory, but the following are anticipated contexts

* /directory/entry/organisation - A top level node for an organisation or legal entity - a whole university, a consortium as a legal entity, vendors, content providers, etc
* /directory/entry/{organisation}/ou/{unit}

Organisations may be split into many organisational units, arranged hierarchically. An organisational unit may have the following sub-resources

* /directory/entry/diku/ou/{ou}/services - Service directory - EG ISO ILL Symbol and service endpoint address
* /directory/entry/diku/ou/{ou}/membership - An OU will list it's membership of consortia and other groupings here
* /directory/entry/diku/ou/{ou}/members - An OU which is a membership organisation may publically list it's members here
* /directory/entry/diku/ou/{ou}/policy - Policy information at the OU level

### Example URLs

* /directory/diku/services/isoill
* /directory/diku/services/genericScript
* /directory/diku/ou/computing/services/isoill
* /directory/diku/ou/computing/policy/


### Open Questions
* Should OU entries include all their parent services, or should parent services subsume all the child services??
* Is the OU construct useful/necessary





# Testing

in the service directory, run

    ./gradlew test

To run the test suite.

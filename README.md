# mod-directory

## Grails-okapi

mod-directory is a [https://gitlab.com/knowledge-integration/folio/grails-okapi](grails-okapi) project - see that project for general troubleshooting 
for grails / okapi ecosystem projects.

## For front end and service developers wanting to use the resources mod-directory exposes

mod-directory currently exposes the following resource paths

* [/directory/entry](doc/entry.md) - An entry in the directory
* [/directory/service](doc/service.md) - A service record
* [/directory/serviceAccount](doc/serviceAccount.md) - A service account - linking an entry with a service
* [/directory/tags](doc/tag.md) - Tags

Additionally, this document sets out the primary business use case for mod directory

* [/directory/api](doc/api.md) - A utility endpoint for tools and helpers - In particular, looking up symbols in a namespace

Public endpoints

* [_/invoke/tenant/reshare_south/directory/externalApi/entry](List public directory entries hosted by this tenant)
* [_/invoke/tenant/reshare_south/directory/externalApi/entry/SLUG](Detailed record for SLUG)

Some specific and common stories 

* [Find a service account for a given entry/service pair](doc/discovery.md)

## Purpose

Mod-Directory is a service intiailly proposed for use in resource sharing environments but offering
services to any library scenario that involves documenting and using service interactions between peers. 
It's purpose is to provide a bounded context for the storage, retrieval and update
of directory service information which underpins inter-library services. This directory contains, but is not restricted to

* Organisations and Organisational Units (Including Instituitons and logical groups such as cosortia and regional assemblies)
* Service Endpoints within organisations
* Policy and Group Memberships (Consortia)
* Registries - Organisations who collate and maintain directories - EG OLF may become a focal point for registry activity

Mod-Directory acts as a means for an institution to maintain and publish it's own directory information, and as a repository for this information about remote or external
organisations, services and policies. Mod-Directory is the definitive local cache of partner institution information. If an application needs to know the preferred 
protocol for issuing an ILL request to a peer institution with whom we have a reciprocal lending agreement, mod-directory is responsible for answering that question.

### Rationale

A headache in the current arrangement of peer lending within libraries has been the conflation of directory service information with servicies. Vendors exercise a kind of
"Regulatory Capture" with service directories where the maintenance of a directory becomes a walled garden within which goods and services must be exchanged. mod-directory
attempts to put control over service arrangements back in the hands of institutions - who can choose from a multitude of consortia and interlending agreements.

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

# Technical

mod-directory is a grails 4 REST profile application which uses liquibase migrations for tenant schema construction.
Migrations can be found [here](https://github.com/openlibraryenvironment/mod-directory/tree/master/service/grails-app/migrations)



## Testing

in the service directory, run

    ./gradlew test

To run the test suite.

## Regenerating liquibase migrations

The grails application.yml specifies an environment called dbGen which can be used by developers who are
using an okapi testing image as their baseline system. Use this named configuraiton when trying to regenerate
the migrations using one of the following commands:

    grails -Dgrails.env=dbGen dbm-gorm-diff description-of-change.groovy --add
    grails -Dgrails.env=dbGen dbm-generate-gorm-changelog my-new-changelog.groovy


## Kubernetes Deployment Notes

You may need to set OKAPI_SERVICE_PORT and/or OKAPI_SERVICE_HOST on the mod-directory container.

| Env var | Description | Default |
| --- | --- | --- |
|JAVA_OPTIONS|JDK Flags - N.B. MaxRAMPercentage|-server -XX:+UseContainerSupport -XX:MaxRAMPercentage=55.0 -XX:+PrintFlagsFinal|
|DB_HOST|Postgres DB host name|postgres|
|DB_PORT|Postgres DB port|5432|
|DB_USERNAME|Postgres DB username||
|DB_PASSWORD|Postgres DB password||
|DB_DATABASE|Postgres DB|okapi_modules|
|DB_MAXPOOLSIZE|Max DB connection Pool size|50|
|FOLIO_DIRECTORY_DEFUALTTTL|Default time to live for directory entry before a refresh can be attempted - default 2 mins|120000





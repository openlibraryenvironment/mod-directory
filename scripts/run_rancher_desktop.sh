#!/bin/bash

# This script is for executing a double check that the endpoint calls that work directly
# also work via okapi.

# see https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-external-config.html for info on overriding 
# spring boot app config on the command line

# This script should be run from the mod-directory/service directory after a "grails prod war" build completes

the_jar_file=`ls build/libs/mod-directory*.jar | tail -n 1`


echo Start mod-directory in external-register mode

# curl --header "X-Okapi-Tenant: diku" http://localhost:9130/content -X GET

# THis DOES work as expected however - 
java -Djava.net.preferIPv4Stack=true -jar $the_jar_file -Xmx1G --grails.server.host=10.0.2.2 --dataSource.username=okapi_1 --dataSource.password=okapi_1 --dataSource.url=jdbc:postgresql://localhost:30101/okapi_modules


#!/bin/bash

AUTH_TOKEN=`./okapi-login`

echo Got auth token $AUTH_TOKEN

echo "Visit FOAF URL for the OLF"
curl -sSL --header "X-Okapi-Tenant: diku" -H "X-Okapi-Token: $AUTH_TOKEN" -H "Content-Type: application/json" -X GET "http://localhost:9130/directory/settings/foaf"

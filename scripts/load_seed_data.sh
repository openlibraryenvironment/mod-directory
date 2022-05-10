#!/bin/bash

# setOkapiUrl sets the variable OKAPI_URL
. ./setOkapiUrl

FILE_TO_LOAD="../seed_data/olf.json"
AUTH_TOKEN=`./okapi-login`

echo Got auth token $AUTH_TOKEN
echo Loading file ${FILE_TO_LOAD}

echo "Visit FOAF URL for the OLF"
curl -sSL --header "X-Okapi-Tenant: diku" -H "X-Okapi-Token: $AUTH_TOKEN" -H "Content-Type: application/json" -X GET "${OKAPI_URL}/directory/api/addFriend?friendUrl=https://raw.githubusercontent.com/openlibraryenvironment/mod-directory/master/seed_data/${FILE_TO_LOAD}"

#!/bin/bash

AUTH_TOKEN=`./okapi-login`

echo Got auth token $AUTH_TOKEN

echo "Visit FOAF URL for the OLF"
curl -sSL --header "X-Okapi-Tenant: diku" -H "X-Okapi-Token: $AUTH_TOKEN" -H "Content-Type: application/json" -X GET "http://localhost:9130/directory/api/addFriend?friendUrl=https://raw.githubusercontent.com/openlibraryenvironment/mod-directory/master/seed_data/localdev/localnet.json"

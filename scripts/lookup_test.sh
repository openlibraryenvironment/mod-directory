#!/bin/bash

AUTH_TOKEN=`./okapi-login`

echo Got auth token $AUTH_TOKEN

echo Listing current entries for symbol BRYN

curl -sSL --header "X-Okapi-Tenant: diku" -H "X-Okapi-Token: $AUTH_TOKEN" -H "Content-Type: application/json" -X GET "http://localhost:9130/directory/entry?filters=symbols.symbol%3dBRYN&stats=true"

echo Listing current entries for symbol PALCI:BRYN - will be the same
curl -sSL --header "X-Okapi-Tenant: diku" -H "X-Okapi-Token: $AUTH_TOKEN" -H "Content-Type: application/json" -X GET "http://localhost:9130/directory/entry?filters=symbols.symbol%3dBRYN&filters=symbols.authority.value=PALCI&stats=true"

echo Test false result - no records
curl -sSL --header "X-Okapi-Tenant: diku" -H "X-Okapi-Token: $AUTH_TOKEN" -H "Content-Type: application/json" -X GET "http://localhost:9130/directory/entry?filters=symbols.symbol%3dBRYN&filters=symbols.authority.symbol%3dWIBBLE&stats=true"

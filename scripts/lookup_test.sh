#!/bin/bash

AUTH_TOKEN=`./okapi-login`

echo
echo Got auth token $AUTH_TOKEN
echo
echo Listing current entries for symbol BRYN
echo

curl -sSL --header "X-Okapi-Tenant: diku" -H "X-Okapi-Token: $AUTH_TOKEN" -H "Content-Type: application/json" -X GET "http://localhost:9130/directory/entry?filters=symbols.symbol%3dBRYN&stats=true"

echo
echo Listing current entries for symbol PALCI:BRYN - will be the same
echo
curl -sSL --header "X-Okapi-Tenant: diku" -H "X-Okapi-Token: $AUTH_TOKEN" -H "Content-Type: application/json" -X GET "http://localhost:9130/directory/entry?filters=symbols.symbol%3dBRYN&filters=symbols.authority.value=PALCI&stats=true"

echo
echo Test false result - no records
echo
curl -sSL --header "X-Okapi-Tenant: diku" -H "X-Okapi-Token: $AUTH_TOKEN" -H "Content-Type: application/json" -X GET "http://localhost:9130/directory/entry?filters=symbols.symbol%3dBRYN&filters=symbols.authority.symbol%3dWIBBLE&stats=true"


echo
echo Find the ILL service for PALCI:BRYN - this gives us the protocol and the preferred namespace
echo
curl -sSL --header "X-Okapi-Tenant: diku" -H "X-Okapi-Token: $AUTH_TOKEN" -H "Content-Type: application/json" -X GET "http://localhost:9130/directory/serviceAccount?filters=accountHolder.symbols.symbol%3dBRYN&filters=accountHolder.symbols.authority.symbol%3dPALCI&filters=service.businessFunction.value%3dill&stats=true"

# echo Directory test
# 
# BRYN_REC=`curl -sSL --header "X-Okapi-Tenant: diku" -H "X-Okapi-Token: $AUTH_TOKEN" -H "Content-Type: application/json" -X GET "http://localhost:9130/directory/entry?filters=symbols.symbol%3dBRYN&filters=symbols.authority.value=PALCI&stats=true"`
# BRYN_ID=""
# BLOOMSBURG_REC=`curl -sSL --header "X-Okapi-Tenant: diku" -H "X-Okapi-Token: $AUTH_TOKEN" -H "Content-Type: application/json" -X GET "http://localhost:9130/directory/entry?filters=symbols.symbol%3dBLOOMMAIN&filters=symbols.authority.value=PALCI&stats=true"`
# BLOOMSBURG_ID=""



# Lookup process
# Find me all the service records for a given symbol
# For each service
# Find me a pair of symbols in the preferred namespace by traversing the tree

#!/bin/bash

AUTH_TOKEN=`./okapi-login`

echo Got auth token $AUTH_TOKEN

echo Listing current entries
curl -sSL --header "X-Okapi-Tenant: diku" -H "X-Okapi-Token: $AUTH_TOKEN" -H "Content-Type: application/json" -X GET http://localhost:9130/directory/entry 

echo
echo lookup the service for loopback-iso-18626
loopback_svc_rec=`curl -sSL --header "X-Okapi-Tenant: diku" -H "X-Okapi-Token: $AUTH_TOKEN" -H "Content-Type: application/json" -X GET http://localhost:9130/directory/service?filter=name%3dloopback-iso-18626`
loopback_svc_id=`echo $loopback_svc_rec | jq -r ".[0].id" | tr -d '\r'`
echo loopback svc $loopback_svc_id : $loopback_svc_rec

echo Create a directory entry for diku
diku_entry_rec=`curl -sSL -H "X-Okapi-Tenant: diku" -H "X-Okapi-Token: $AUTH_TOKEN" -H "Content-Type: application/json" -X POST http://localhost:9130/directory/entry -d ' {
  "name":"diku",
  "slug":"diku",
  "description":"diku",
  "tags":[
    "Testdata"
  ]
}'`

diku_entry_id=`echo $diku_entry_rec | jq -r ".id" | tr -d '\r'`
echo $diku_entry_id : $diku_entry_rec


echo Create a serviceAccount entry for the loopback-iso-18626 service and entry diku
diku_loopback_rec=`curl -sSL -H "X-Okapi-Tenant: diku" -H "X-Okapi-Token: $AUTH_TOKEN" -H "Content-Type: application/json" -X POST http://localhost:9130/directory/serviceAccount -d ' {
  "service": {id: "'"$loopback_svc_id"'" },
  "accountHolder":{id: "'"$diku_entry_id"'" },
  "accountDetails":"maybe_a_symbol"
}'`

echo Diku loopback service: $diku_loopback_rec

echo Look up the serviceAccount for diku and loopback-iso-18626
diku_iso_svc_lookup=`curl -sSL --header "X-Okapi-Tenant: diku" -H "X-Okapi-Token: $AUTH_TOKEN" -H "Content-Type: application/json" -X GET "http://localhost:9130/directory/serviceAccount?filter=service.name%3dloopback-iso-18626&accountHolder.name%3ddiku"`

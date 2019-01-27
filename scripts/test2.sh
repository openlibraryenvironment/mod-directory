AUTH_TOKEN=`./okapi-login`

echo Got auth token $AUTH_TOKEN

echo Listing current entries
curl --header "X-Okapi-Tenant: diku" -H "X-Okapi-Token: $AUTH_TOKEN" -H "Content-Type: application/json" -X GET http://localhost:9130/directory/entry 

# create the url custom property
echo
echo Create url custom property
curl -sSL -H 'Accept:application/json' \
	  -H 'Content-Type: application/json' \
	  -H 'X-OKAPI-TENANT: diku' \
	  -H "X-Okapi-Token: $AUTH_TOKEN" \
	  -X POST http://localhost:9130/directory/custprops -d '{
 "name":"url", 
 "type":"Text"
}'

echo
echo Create iso-18626 loopback service
DirEnt1=`curl -sSL -H "X-Okapi-Tenant: diku" -H "X-Okapi-Token: $AUTH_TOKEN" -H "Content-Type: application/json" -X POST http://localhost:9130/directory/entry -d ' {
  "name":"diku",
  "slug":"diku",
  "description":"diku",
  "tags":[
    "Testdata", "TestRun1", "MonographTest"
  ],
  "customProperties":{
    "url": ["http://localhost:9130/rs/iso18626"]
  }
}'`

echo
echo Created request 1: $DirEnt1

service_1=`curl -sSL -H "X-Okapi-Tenant: diku" -H "X-Okapi-Token: $AUTH_TOKEN" -H "Content-Type: application/json" -X POST http://localhost:9130/directory/service -d ' {
  name:"loopback-iso-18626",
  type:"ISO18626",
  tags:[
    "Testdata", "TestRun1", "MonographTest"
  ],
  customProperties:{
    "url": ["http://localhost:9130/rs/iso18626"]
  }
}
'`
echo
echo Created service 1: $service_1


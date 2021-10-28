#! /bin/sh

AUTH_TOKEN=`./okapi-login`
USER_QUERY_RESPONSE=$(curl -sSL -H "X-Okapi-Tenant:diku" -H "X-Okapi-Token:$AUTH_TOKEN" \
    -H "Content-Type: application/json" \
    -X GET "http://localhost:9130/users?query=username=diku_admin" )
# echo qr : $USER_QUERY_RESPONSE
USER_ID=$(echo $USER_QUERY_RESPONSE | jq -r '.users | .[0] | .id')
echo $USER_ID

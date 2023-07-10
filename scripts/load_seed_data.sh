#!/bin/bash

# If we are using the rancher desktop then there will be a .okapirc file
if [ -f $HOME/.okapirc ]; then
	# Everything is set in .okapirc
    . $HOME/.okapirc
else
	# setOkapiUrl sets the variable OKAPI_URL
	. ./setOkapiUrl
fi

FILE_TO_LOAD="../seed_data/olf.json"
#FILE_TO_LOAD="../seed_data/localdev/localnet.json"
#FILE_TO_LOAD="../seed_data/localdev/localhosta.json"
#FILE_TO_LOAD="../seed_data/localdev/localhostb.json"
#FILE_TO_LOAD="../seed_data/localdev/localhostc.json"
#FILE_TO_LOAD="../seed_data/KnowInt.json"

AUTH_TOKEN=`./okapi-login -u ${UN} -p ${PW} -t ${TENANT}`

echo Got auth token $AUTH_TOKEN
echo Loading file ${FILE_TO_LOAD} into tenant ${TENANT}

echo "Visit FOAF URL for the OLF"
curl -sSL --header "X-Okapi-Tenant: ${TENANT}" -H "X-Okapi-Token: $AUTH_TOKEN" -H "Content-Type: application/json" -X GET "${OKAPI_URL}/directory/api/addFriend?friendUrl=https://raw.githubusercontent.com/openlibraryenvironment/mod-directory/master/seed_data/${FILE_TO_LOAD}"

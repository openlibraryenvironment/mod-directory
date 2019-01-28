# service discovery

There are 2 primary use cases that can occour that need support from the directory service:

* 1. I've located an item in the shared catalog - and have a library identifier. I need to look up a service in order to know how and where to send a request to. Often, libraries will have a protocol specific symbol that we need to look up based on institution and protocol.
* 2. I'm involved in a conversation with a partner organisation, I have a symbol and a protcol - I need to look up the details of how to connect. In this case, we likely know a protocol and a symbol and want the connect details instead.

The following curl command is copied from the test2.sh script

    curl -sSL -H "X-Okapi-Tenant: diku" \
              -H "X-Okapi-Token: $AUTH_TOKEN" \
              -H "Content-Type: application/json" \
              -X GET "http://localhost:9130/directory/serviceAccount?filter=service.name%3dloopback-iso-18626&accountHolder.name%3ddiku"

This command is asking mod-directory to list all serviceAccount records for services with the name loopback-iso-18626 and the account holder diku. Of course it's not likely we would know the name of the service we want, only the type. This command will list all ISO18626 service account entries for diku by filtering on service.type.value == ISO18626 and accountHolder.name == diku

    curl -sSL -H "X-Okapi-Tenant: diku" \
              -H "X-Okapi-Token: $AUTH_TOKEN" \
              -H "Content-Type: application/json" \
              -X GET "http://localhost:9130/directory/serviceAccount?filter=service.type.value%3dISO18626&accountHolder.name%3ddiku"

Given the default test setup, both calls should return one record as follows (Provided for illustative purposes, your content may vary):

    [{
	"id": "ff8081816894d32b016894d415aa000c",
	"accountHolder": {
		"id": "ff8081816894d32b016894d4148d000b",
		"customProperties": {
			"id": 5
		},
		"slug": "diku",
		"tags": [{
			"id": 4
		}],
		"name": "diku",
		"description": "diku"
	},
	"customProperties": {},
	"service": {
		"id": "ff8081816894d32b016894d3fbc6000a",
		"customProperties": {
			"url": [{
				"id": 3,
				"value": "http://localhost:9130/rs/iso18626",
				"type": "class com.k_int.web.toolkit.custprops.types.CustomPropertyText"
			}]
		},
		"tags": [{
			"id": 1,
			"normValue": "system-default",
			"value": "system-default"
		}],
		"name": "loopback-iso-18626",
		"type": {
			"id": "ff8081816894d32b016894d3f4470004",
			"value": "iso18626",
			"label": "Iso18626",
			"owner": {
				"id": "ff8081816894d32b016894d3f3a40000",
				"desc": "Service.Type"
			}
		}
	},
	"accountDetails": "maybe_a_symbol"
    }]

Implementations must be prepared to deal with multiple service accounts.

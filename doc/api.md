# mod-directory API endpoint

The primary use case for mod-directory is : 

## Given

* A discovery search result which suggests a resource may be available at a remote location,
* A namespace and a symbol for that remote location
* A namespace and a symbol (Or some other means to identify) the local location

## Then 

* Allow the system to locate the protocol details of a remote service that might accept a resource sharing request
* Having located those details, determine what the preferred naming authority in use at the remote system is
* See if both the remote and local locations have symbols that reside in the preferred namespace.

mod-directory solves this problem in two steps. Firstly, a system can request the directory entry of both the remote and local locations
with the standard search API

# Step 1 - Look up the directory entry ID for the different locations

Given a possible responder peer with location PALCI:BRYN

    curl -sSL -H "X-Okapi-Tenant: diku" \
              -H "X-Okapi-Token: $AUTH_TOKEN" \
              -H "Content-Type: application/json" \
         -X GET "http://localhost:9130/directory/entry?filters=symbols.symbol%3dBRYN&filters=symbols.authority.value=PALCI&stats=true"

Here the parameters
* filters=symbol.symbol%3dBRYN -- symbol.symbol = BRYN
* filters=symbol.authority.value%3dPALIC -- symbol.authority.value = PALCI

are anded together to make a search for symbol PALIC:BRYN. This record will return the directory entry for this institution


# Step 2 - what we really need however is to find a service we can use to send a request to PALCI:BRYN

    curl -sSL -H "X-Okapi-Tenant: diku" \
              -H "X-Okapi-Token: $AUTH_TOKEN" \
              -H "Content-Type: application/json" \
              -X GET "http://localhost:9130/directory/serviceAccount?filters=accountHolder.symbols.symbol%3dBRYN&filters=accountHolder.symbols.authority.symbol%3dPALCI&filters=service.businessFunction.value%3dill&stats=true"

This command will search for the serviceAccount resource for PALCI:BRYN which supports businessFunction ill. 

The idea here is that a service account might support many different business functions (EG RTAC) and may have many different protocols which support a process (The directory currently registers the following service types for the ILL business function: 'ISO10161.TCP', 'ISO10161.SMTP', 'ISO18626', 'GSM.SMTP').

This URL will return the following JSON


    {
	"results": [{
		"id": "ff80818168d1b4470168d1b643830029",
		"accountHolder": {
                        "id": "ff80818168d1b4470168d1b643810025",
			"name": "Bryn Mawr College",
                        <<SNIP>>
		},
		"customProperties": {
			"ILLPreferredNamespaces": [{
				"id": 23,
				"value": "IDS",
				"type": "class com.k_int.web.toolkit.custprops.types.CustomPropertyText"
			}, {
				"id": 22,
				"value": "PALCI",
				"type": "class com.k_int.web.toolkit.custprops.types.CustomPropertyText"
			}]
		},
		"service": {
			"id": "ff80818168d1b4470168d1b640620016",
			"address": "https://localhost/reshare/iso18626",
			"customProperties": {},
			"tags": [],
			"name": "ReShare ISO18626 Service",
			"type": {
				"id": "ff80818168d1b4470168d1b4bb690004",
				"value": "iso18626",
				"label": "Iso18626",
				"owner": {
					"id": "ff80818168d1b4470168d1b4bac70000",
					"desc": "Service.Type"
				}
			},
			"businessFunction": {
				"id": "ff80818168d1b4470168d1b4bbbf000a"
			}
		}
	}],
	"pageSize": 10,
	"page": 1,
	"totalPages": 1,
	"meta": {},
	"totalRecords": 1,
	"total": 1
    }


Some of the details are omitted for clarity, but note the service entry which gives the address / URL of the endpoint, and the type specifier which tells
the caller this is an ISO18626 endpoint. Note also the top level customProperties section, which tells us that the preferred namespaces for ISO18626 at Bryn Mawr College
are IDS and PALCI. In normal use, there will be only one.

We now know how and where we can send a resource sharing request to. The service account record indicates however that we should identify outselves using the PALCI
namespace. By using the ID of the directory entry (accountHolder.id - ff80818168d1b4470168d1b643810025 in the json above) and the target namespace (PALCI) we can then find the symbols we need

   curl -sSL -H "X-Okapi-Tenant: diku" \ 
             -H "X-Okapi-Token: $AUTH_TOKEN" \ 
             -H "Content-Type: application/json" \ 
             -X GET "http://localhost:9130/directory/api/findSymbol?for=$ff80818168d1b4470168d1b643810025&ns=PALCI"

this call returns

    {"symbol":"BMC"}

Which is the symbol we should use when sending an ISO18626 request to https://localhost/reshare/iso18626 which is intended for Bryn Mawr College

If we wanted the OCLC symbol for this location we would use

   curl -sSL -H "X-Okapi-Tenant: diku" \ 
             -H "X-Okapi-Token: $AUTH_TOKEN" \ 
             -H "Content-Type: application/json" \ 
             -X GET "http://localhost:9130/directory/api/findSymbol?for=$ff80818168d1b4470168d1b643810025&ns=OCLC"


the same process can be used for the requesting location symbol if needed. If a directory entry does not have a symbol for the given namespace but DOES have
a parent, then the parent records will be checked in turn until a match is found, or the tree is exhausted.

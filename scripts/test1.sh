
curl -XGET --header "X-Okapi-Tenant: diku" http://localhost:8080/dirent
curl -XGET --header "X-Okapi-Tenant: diku" http://localhost:8080/_/tenant
curl -XPOST --header "X-Okapi-Tenant: diku" http://localhost:8080/_/tenant


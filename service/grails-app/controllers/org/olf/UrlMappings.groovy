package org.olf

class UrlMappings {

  static mappings = {

    "/"(controller: 'application', action:'index')

    "/directory/api/findSymbol"(controller: 'application', action:'findSymbol')
    "/directory/api/addFriend"(controller: 'application', action:'addFriend')

    "/directory/entry"(resources:'directoryEntry')
    "/directory/service"(resources:'Service')
    "/directory/serviceAccount"(resources:'ServiceAccount')
    "/directory/namingAuthority"(resources:'NamingAuthority')

    "/directory/kiwt/config/$extended?" (controller: 'directoryConfig' , action: "resources")
    "/directory/kiwt/config/schema/$type" (controller: 'directoryConfig' , action: "schema")
    "/directory/kiwt/config/schema/embedded/$type" (controller: 'directoryConfig' , action: "schemaEmbedded")
    "/directory/kiwt/raml" (controller: 'directoryConfig' , action: "raml")

    "/directory/settings/worker" (controller: 'directorySettings', action: 'worker');
    "/directory/settings/foaf" (controller: 'directorySettings', action: 'foaf');


    // Call /rs/refdata to list all refdata categories
    '/directory/refdata'(resources: 'refdata') {
      collection {
        "/$domain/$property" (controller: 'refdata', action: 'lookup')
      }
    }

    // Call /rs/custprop  to list all custom properties
    '/directory/custprops'(resources: 'customPropertyDefinition')
    '/directory/tags'(resources: 'tags')

    "500"(view: '/error')
    "404"(view: '/notFound')


  }
}

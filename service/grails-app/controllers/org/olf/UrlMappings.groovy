package org.olf

class UrlMappings {

  static mappings = {

    "/"(controller: 'application', action:'index')

    "/directory/api/findSymbol"(controller: 'application', action:'findSymbol')

    "/directory/entry"(resources:'directoryEntry')
    "/directory/service"(resources:'Service')
    "/directory/serviceAccount"(resources:'ServiceAccount')

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

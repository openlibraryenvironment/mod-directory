package org.olf

class UrlMappings {

  static mappings = {

    "/"(controller: 'application', action:'index')
    "/config"(controller: 'application', action:'config')
    "/_/tenant"(controller: 'okapi', action:'tenant')

    "/directory/kiwt/config/$extended?" (controller: 'config' , action: "resources")
    "/directory/kiwt/config/schema/$type" (controller: 'config' , action: "schema")
    "/directory/kiwt/config/schema/embedded/$type" (controller: 'config' , action: "schemaEmbedded")

    delete "/$controller/$id(.$format)?"(action:"delete")
    get "/$controller(.$format)?"(action:"index")
    get "/$controller/$id(.$format)?"(action:"show")
    post "/$controller(.$format)?"(action:"save")
    put "/$controller/$id(.$format)?"(action:"update")
    patch "/$controller/$id(.$format)?"(action:"patch")
    "500"(view: '/error')
    "404"(view: '/notFound')

    "/directory/entry"(resources:'directoryEntry')

  }
}

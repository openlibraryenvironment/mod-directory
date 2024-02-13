package mod.directory

import grails.boot.GrailsApp
import grails.boot.config.GrailsAutoConfiguration

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

@CompileStatic
@Slf4j
class Application extends GrailsAutoConfiguration {

  static void main(String[] args) {
    final TimeZone utcTz = TimeZone.getTimeZone("UTC")
    if (TimeZone.default != utcTz) {
      println("Timezone default is ${TimeZone.default.displayName}. Setting to UTC")
      TimeZone.default = utcTz
    }
    GrailsApp.run(Application, args)
  }
}

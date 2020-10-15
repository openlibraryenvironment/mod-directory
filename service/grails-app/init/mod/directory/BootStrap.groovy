package mod.directory

import com.k_int.okapi.OkapiTenantAdminService

class BootStrap {

  def grailsApplication
  OkapiTenantAdminService okapiTenantAdminService

  def init = { servletContext ->

    log.info("mod-directory (${grailsApplication.metadata['app.version']}) initialising");
    log.info("          build number -> ${grailsApplication.metadata['build.number']}");
    log.info("        build revision -> ${grailsApplication.metadata['build.git.revision']}");
    log.info("          build branch -> ${grailsApplication.metadata['build.git.branch']}");
    log.info("          build commit -> ${grailsApplication.metadata['build.git.commit']}");
    log.info("            build time -> ${grailsApplication.metadata['build.time']}");
    log.info("         Base JDBC URL -> ${grailsApplication.config.dataSource.url}");

    okapiTenantAdminService.freshenAllTenantSchemas()
  }


  def destroy = {
  }
}

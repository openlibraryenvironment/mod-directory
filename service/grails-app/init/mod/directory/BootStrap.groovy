package mod.directory

import com.k_int.okapi.OkapiTenantAdminService

class BootStrap {

    OkapiTenantAdminService okapiTenantAdminService
    def init = { servletContext ->
  
      okapiTenantAdminService.freshenAllTenantSchemas()
    }
    def destroy = {
    }
}

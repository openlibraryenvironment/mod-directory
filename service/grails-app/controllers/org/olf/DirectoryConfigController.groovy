package mod.rs;

import com.k_int.web.toolkit.ConfigController;

import grails.converters.JSON

public class DirectoryConfigController extends ConfigController {

private static String raml_text = '''
#%RAML 1.0

title: Directory API
baseUri: https://github.com/openlibraryenvironment/mod-directory
version: v1

documentation:
  - title: mod-directory API
    content: This documents the API calls that can be made to query and manage directory resources

'''

  def raml() {
    // yaml can be application/x-yaml or 
    render ( text: raml_text )
  }


}



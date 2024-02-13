databaseChangeLog = {
  include file: 'initial-customisations.groovy'
  include file: 'create-mod-directory.groovy'
  include file: 'update-mod-directory-2-7.groovy'

  // Pulled in from web-toolkit-ce
  include file: 'wtk/additional_CustomPropertyDefinitions.feat.groovy'
  include file: 'wtk/multi-value-custprops.feat.groovy'
  include file: 'wtk/hidden-appsetting.feat.groovy'
}

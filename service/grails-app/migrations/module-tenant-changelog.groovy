databaseChangeLog = {
  include file: 'initial-customisations.groovy'
  // When we are ready - doing this instead should include the migrations from dm-directory rather than having them locally
  // include file: 'directory/create-mod-directory.groovy'
  // When we do this we shoud also pull the settings migrations from web-toolkit rather than having them embedded here
  include file: 'create-mod-directory.groovy'
  include file: 'update-mod-directory-2-7.groovy'
}

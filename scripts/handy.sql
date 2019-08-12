psql -U folio_admin -h localhost okapi_modules

set search_path to diku_mod_directory, public;


DROP DATABASE olftest;
CREATE DATABASE olftest;
GRANT ALL PRIVILEGES ON DATABASE olftest to folio_admin;



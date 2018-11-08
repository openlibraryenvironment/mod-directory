databaseChangeLog = {

    changeSet(author: "ibbo (generated)", id: "1541692532868-1") {
        createSequence(sequenceName: "hibernate_sequence")
    }

    changeSet(author: "ibbo (generated)", id: "1541692532868-2") {
        createTable(tableName: "boolean_property") {
            column(name: "id", type: "BIGINT") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "ibbo (generated)", id: "1541692532868-3") {
        createTable(tableName: "directory_entry") {
            column(name: "de_id", type: "VARCHAR(36)") {
                constraints(nullable: "false")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "de_slug", type: "VARCHAR(255)")

            column(name: "de_name", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }

            column(name: "de_desc", type: "VARCHAR(255)")
        }
    }

    changeSet(author: "ibbo (generated)", id: "1541692532868-4") {
        createTable(tableName: "property") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(primaryKey: "true", primaryKeyName: "propertyPK")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "name", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }

            column(name: "value", type: "BYTEA") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "ibbo (generated)", id: "1541692532868-5") {
        createTable(tableName: "property_definition") {
            column(name: "pd_id", type: "VARCHAR(36)") {
                constraints(nullable: "false")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "pd_name", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }

            column(name: "pd_type", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }

            column(name: "pd_rdc", type: "VARCHAR(255)")

            column(name: "pd_description", type: "VARCHAR(255)")
        }
    }

    changeSet(author: "ibbo (generated)", id: "1541692532868-6") {
        createTable(tableName: "refdata_category") {
            column(name: "rdc_id", type: "VARCHAR(36)") {
                constraints(nullable: "false")
            }

            column(name: "rdc_version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "rdc_description", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }

            column(name: "rdc_label", type: "VARCHAR(255)")
        }
    }

    changeSet(author: "ibbo (generated)", id: "1541692532868-7") {
        createTable(tableName: "refdata_value") {
            column(name: "rdv_id", type: "VARCHAR(36)") {
                constraints(nullable: "false")
            }

            column(name: "rdv_version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "rdv_use_instead", type: "VARCHAR(36)")

            column(name: "rdv_icon", type: "VARCHAR(255)")

            column(name: "rdv_value", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }

            column(name: "rdv_owner", type: "VARCHAR(36)") {
                constraints(nullable: "false")
            }

            column(name: "rdv_desc", type: "VARCHAR(64)")

            column(name: "rdv_sortkey", type: "VARCHAR(255)")
        }
    }

    changeSet(author: "ibbo (generated)", id: "1541692532868-8") {
        createTable(tableName: "refdata_value_label") {
            column(name: "rdvl_id", type: "VARCHAR(36)") {
                constraints(nullable: "false")
            }

            column(name: "rdvl_version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "rdvl_locale", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }

            column(name: "rdvl_owner", type: "VARCHAR(36)") {
                constraints(nullable: "false")
            }

            column(name: "label", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "ibbo (generated)", id: "1541692532868-9") {
        createTable(tableName: "text_property") {
            column(name: "id", type: "BIGINT") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "ibbo (generated)", id: "1541692532868-10") {
        addPrimaryKey(columnNames: "id", constraintName: "boolean_propertyPK", tableName: "boolean_property")
    }

    changeSet(author: "ibbo (generated)", id: "1541692532868-11") {
        addPrimaryKey(columnNames: "de_id", constraintName: "directory_entryPK", tableName: "directory_entry")
    }

    changeSet(author: "ibbo (generated)", id: "1541692532868-12") {
        addPrimaryKey(columnNames: "pd_id", constraintName: "property_definitionPK", tableName: "property_definition")
    }

    changeSet(author: "ibbo (generated)", id: "1541692532868-13") {
        addPrimaryKey(columnNames: "rdc_id", constraintName: "refdata_categoryPK", tableName: "refdata_category")
    }

    changeSet(author: "ibbo (generated)", id: "1541692532868-14") {
        addPrimaryKey(columnNames: "rdv_id", constraintName: "refdata_valuePK", tableName: "refdata_value")
    }

    changeSet(author: "ibbo (generated)", id: "1541692532868-15") {
        addPrimaryKey(columnNames: "rdvl_id", constraintName: "refdata_value_labelPK", tableName: "refdata_value_label")
    }

    changeSet(author: "ibbo (generated)", id: "1541692532868-16") {
        addPrimaryKey(columnNames: "id", constraintName: "text_propertyPK", tableName: "text_property")
    }

    changeSet(author: "ibbo (generated)", id: "1541692532868-17") {
        addUniqueConstraint(columnNames: "pd_name", constraintName: "UC_PROPERTY_DEFINITIONPD_NAME_COL", tableName: "property_definition")
    }

    changeSet(author: "ibbo (generated)", id: "1541692532868-18") {
        createIndex(indexName: "rdc_description_idx", tableName: "refdata_category") {
            column(name: "rdc_description")
        }
    }

    changeSet(author: "ibbo (generated)", id: "1541692532868-19") {
        createIndex(indexName: "rdv_entry_idx", tableName: "refdata_value") {
            column(name: "rdv_value")

            column(name: "rdv_owner")
        }
    }

    changeSet(author: "ibbo (generated)", id: "1541692532868-20") {
        createIndex(indexName: "rdvl_entry_idx", tableName: "refdata_value_label") {
            column(name: "rdvl_locale")

            column(name: "rdvl_owner")
        }
    }

    changeSet(author: "ibbo (generated)", id: "1541692532868-21") {
        createIndex(indexName: "td_type_idx", tableName: "property_definition") {
            column(name: "pd_type")

            column(name: "pd_rdc")
        }
    }

    changeSet(author: "ibbo (generated)", id: "1541692532868-22") {
        addForeignKeyConstraint(baseColumnNames: "rdvl_owner", baseTableName: "refdata_value_label", constraintName: "FKclotewcgrrbmoy8gcyakn3u93", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "rdv_id", referencedTableName: "refdata_value")
    }

    changeSet(author: "ibbo (generated)", id: "1541692532868-23") {
        addForeignKeyConstraint(baseColumnNames: "rdv_owner", baseTableName: "refdata_value", constraintName: "FKh4fon2a7k4y8b2sicjm0i6oy8", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "rdc_id", referencedTableName: "refdata_category")
    }

    changeSet(author: "ibbo (generated)", id: "1541692532868-24") {
        addForeignKeyConstraint(baseColumnNames: "rdv_use_instead", baseTableName: "refdata_value", constraintName: "FKpk86botisjrgyfd5aqljbwla", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "rdv_id", referencedTableName: "refdata_value")
    }
}

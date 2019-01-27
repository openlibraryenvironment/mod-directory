databaseChangeLog = {

    changeSet(author: "ianibbo (generated)", id: "1548593720305-1") {
        createSequence(sequenceName: "hibernate_sequence")
    }

    changeSet(author: "ianibbo (generated)", id: "1548593720305-2") {
        createTable(tableName: "custom_property") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(primaryKey: "true", primaryKeyName: "custom_propertyPK")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "definition_id", type: "VARCHAR(36)")

            column(name: "parent_id", type: "BIGINT")
        }
    }

    changeSet(author: "ianibbo (generated)", id: "1548593720305-3") {
        createTable(tableName: "custom_property_blob") {
            column(name: "id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "value", type: "OID") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "ianibbo (generated)", id: "1548593720305-4") {
        createTable(tableName: "custom_property_boolean") {
            column(name: "id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "value", type: "BOOLEAN") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "ianibbo (generated)", id: "1548593720305-5") {
        createTable(tableName: "custom_property_container") {
            column(name: "id", type: "BIGINT") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "ianibbo (generated)", id: "1548593720305-6") {
        createTable(tableName: "custom_property_decimal") {
            column(name: "id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "value", type: "NUMBER(19, 2)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "ianibbo (generated)", id: "1548593720305-7") {
        createTable(tableName: "custom_property_definition") {
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

            column(name: "pd_description", type: "VARCHAR(255)")
        }
    }

    changeSet(author: "ianibbo (generated)", id: "1548593720305-8") {
        createTable(tableName: "custom_property_integer") {
            column(name: "id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "value", type: "INT") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "ianibbo (generated)", id: "1548593720305-9") {
        createTable(tableName: "custom_property_refdata") {
            column(name: "id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "value_id", type: "VARCHAR(36)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "ianibbo (generated)", id: "1548593720305-10") {
        createTable(tableName: "custom_property_refdata_definition") {
            column(name: "pd_id", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }

            column(name: "category_id", type: "VARCHAR(36)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "ianibbo (generated)", id: "1548593720305-11") {
        createTable(tableName: "custom_property_text") {
            column(name: "id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "value", type: "CLOB") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "ianibbo (generated)", id: "1548593720305-12") {
        createTable(tableName: "directory_entry") {
            column(name: "de_id", type: "VARCHAR(36)") {
                constraints(nullable: "false")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "custom_properties_id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "de_slug", type: "VARCHAR(255)")

            column(name: "de_name", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }

            column(name: "de_desc", type: "VARCHAR(255)")

            column(name: "de_parent", type: "VARCHAR(36)")
        }
    }

    changeSet(author: "ianibbo (generated)", id: "1548593720305-13") {
        createTable(tableName: "directory_entry_tag") {
            column(name: "directory_entry_tags_id", type: "VARCHAR(36)") {
                constraints(nullable: "false")
            }

            column(name: "tag_id", type: "BIGINT")
        }
    }

    changeSet(author: "ianibbo (generated)", id: "1548593720305-14") {
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
        }
    }

    changeSet(author: "ianibbo (generated)", id: "1548593720305-15") {
        createTable(tableName: "refdata_value") {
            column(name: "rdv_id", type: "VARCHAR(36)") {
                constraints(nullable: "false")
            }

            column(name: "rdv_version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "rdv_value", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }

            column(name: "rdv_owner", type: "VARCHAR(36)") {
                constraints(nullable: "false")
            }

            column(name: "rdv_label", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "ianibbo (generated)", id: "1548593720305-16") {
        createTable(tableName: "service") {
            column(name: "se_id", type: "VARCHAR(36)") {
                constraints(nullable: "false")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "custom_properties_id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "se_name", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }

            column(name: "se_type_fk", type: "VARCHAR(36)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "ianibbo (generated)", id: "1548593720305-17") {
        createTable(tableName: "service_account") {
            column(name: "sa_id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "sa_account_holder", type: "VARCHAR(36)") {
                constraints(nullable: "false")
            }

            column(name: "sa_service", type: "VARCHAR(36)") {
                constraints(nullable: "false")
            }

            column(name: "sa_account_details", type: "VARCHAR(255)")
        }
    }

    changeSet(author: "ianibbo (generated)", id: "1548593720305-18") {
        createTable(tableName: "service_tag") {
            column(name: "service_tags_id", type: "VARCHAR(36)") {
                constraints(nullable: "false")
            }

            column(name: "tag_id", type: "BIGINT")
        }
    }

    changeSet(author: "ianibbo (generated)", id: "1548593720305-19") {
        createTable(tableName: "tag") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(primaryKey: "true", primaryKeyName: "tagPK")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "norm_value", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }

            column(name: "value", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "ianibbo (generated)", id: "1548593720305-20") {
        addPrimaryKey(columnNames: "id", constraintName: "custom_property_blobPK", tableName: "custom_property_blob")
    }

    changeSet(author: "ianibbo (generated)", id: "1548593720305-21") {
        addPrimaryKey(columnNames: "id", constraintName: "custom_property_booleanPK", tableName: "custom_property_boolean")
    }

    changeSet(author: "ianibbo (generated)", id: "1548593720305-22") {
        addPrimaryKey(columnNames: "id", constraintName: "custom_property_containerPK", tableName: "custom_property_container")
    }

    changeSet(author: "ianibbo (generated)", id: "1548593720305-23") {
        addPrimaryKey(columnNames: "id", constraintName: "custom_property_decimalPK", tableName: "custom_property_decimal")
    }

    changeSet(author: "ianibbo (generated)", id: "1548593720305-24") {
        addPrimaryKey(columnNames: "pd_id", constraintName: "custom_property_definitionPK", tableName: "custom_property_definition")
    }

    changeSet(author: "ianibbo (generated)", id: "1548593720305-25") {
        addPrimaryKey(columnNames: "id", constraintName: "custom_property_integerPK", tableName: "custom_property_integer")
    }

    changeSet(author: "ianibbo (generated)", id: "1548593720305-26") {
        addPrimaryKey(columnNames: "id", constraintName: "custom_property_refdataPK", tableName: "custom_property_refdata")
    }

    changeSet(author: "ianibbo (generated)", id: "1548593720305-27") {
        addPrimaryKey(columnNames: "pd_id", constraintName: "custom_property_refdata_definitionPK", tableName: "custom_property_refdata_definition")
    }

    changeSet(author: "ianibbo (generated)", id: "1548593720305-28") {
        addPrimaryKey(columnNames: "id", constraintName: "custom_property_textPK", tableName: "custom_property_text")
    }

    changeSet(author: "ianibbo (generated)", id: "1548593720305-29") {
        addPrimaryKey(columnNames: "de_id", constraintName: "directory_entryPK", tableName: "directory_entry")
    }

    changeSet(author: "ianibbo (generated)", id: "1548593720305-30") {
        addPrimaryKey(columnNames: "rdc_id", constraintName: "refdata_categoryPK", tableName: "refdata_category")
    }

    changeSet(author: "ianibbo (generated)", id: "1548593720305-31") {
        addPrimaryKey(columnNames: "rdv_id", constraintName: "refdata_valuePK", tableName: "refdata_value")
    }

    changeSet(author: "ianibbo (generated)", id: "1548593720305-32") {
        addPrimaryKey(columnNames: "se_id", constraintName: "servicePK", tableName: "service")
    }

    changeSet(author: "ianibbo (generated)", id: "1548593720305-33") {
        addPrimaryKey(columnNames: "sa_id", constraintName: "service_accountPK", tableName: "service_account")
    }

    changeSet(author: "ianibbo (generated)", id: "1548593720305-34") {
        addUniqueConstraint(columnNames: "pd_name", constraintName: "UC_CUSTOM_PROPERTY_DEFINITIONPD_NAME_COL", tableName: "custom_property_definition")
    }

    changeSet(author: "ianibbo (generated)", id: "1548593720305-35") {
        createIndex(indexName: "rdv_entry_idx", tableName: "refdata_value") {
            column(name: "rdv_value")

            column(name: "rdv_owner")
        }
    }

    changeSet(author: "ianibbo (generated)", id: "1548593720305-36") {
        createIndex(indexName: "td_type_idx", tableName: "custom_property_definition") {
            column(name: "pd_type")
        }
    }

    changeSet(author: "ianibbo (generated)", id: "1548593720305-37") {
        addForeignKeyConstraint(baseColumnNames: "de_parent", baseTableName: "directory_entry", constraintName: "FK1lcdvuk9hkmebm544kwmxoclj", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "de_id", referencedTableName: "directory_entry")
    }

    changeSet(author: "ianibbo (generated)", id: "1548593720305-38") {
        addForeignKeyConstraint(baseColumnNames: "custom_properties_id", baseTableName: "directory_entry", constraintName: "FK2qp9dd004mntrub21o6djlxqh", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "custom_property_container")
    }

    changeSet(author: "ianibbo (generated)", id: "1548593720305-39") {
        addForeignKeyConstraint(baseColumnNames: "definition_id", baseTableName: "custom_property", constraintName: "FK36grvth72fb7wu5i5xaeqjitw", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "pd_id", referencedTableName: "custom_property_definition")
    }

    changeSet(author: "ianibbo (generated)", id: "1548593720305-40") {
        addForeignKeyConstraint(baseColumnNames: "se_type_fk", baseTableName: "service", constraintName: "FK37qd0xlyn5tpy48wega3ss3hy", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "rdv_id", referencedTableName: "refdata_value")
    }

    changeSet(author: "ianibbo (generated)", id: "1548593720305-41") {
        addForeignKeyConstraint(baseColumnNames: "value_id", baseTableName: "custom_property_refdata", constraintName: "FK5ogn0fedwxxy4fhmq9du4qej2", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "rdv_id", referencedTableName: "refdata_value")
    }

    changeSet(author: "ianibbo (generated)", id: "1548593720305-42") {
        addForeignKeyConstraint(baseColumnNames: "directory_entry_tags_id", baseTableName: "directory_entry_tag", constraintName: "FK73prfacykqmx20o3gr9dr7b98", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "de_id", referencedTableName: "directory_entry")
    }

    changeSet(author: "ianibbo (generated)", id: "1548593720305-43") {
        addForeignKeyConstraint(baseColumnNames: "category_id", baseTableName: "custom_property_refdata_definition", constraintName: "FKbrh88caagajlvrpaydg4tr3qx", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "rdc_id", referencedTableName: "refdata_category")
    }

    changeSet(author: "ianibbo (generated)", id: "1548593720305-44") {
        addForeignKeyConstraint(baseColumnNames: "parent_id", baseTableName: "custom_property", constraintName: "FKd5u2tgpracxvk1xw8pdreuj5h", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "custom_property_container")
    }

    changeSet(author: "ianibbo (generated)", id: "1548593720305-45") {
        addForeignKeyConstraint(baseColumnNames: "rdv_owner", baseTableName: "refdata_value", constraintName: "FKh4fon2a7k4y8b2sicjm0i6oy8", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "rdc_id", referencedTableName: "refdata_category")
    }

    changeSet(author: "ianibbo (generated)", id: "1548593720305-46") {
        addForeignKeyConstraint(baseColumnNames: "sa_account_holder", baseTableName: "service_account", constraintName: "FKl0sums8w3h2i90a7gudkkvs6", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "de_id", referencedTableName: "directory_entry")
    }

    changeSet(author: "ianibbo (generated)", id: "1548593720305-47") {
        addForeignKeyConstraint(baseColumnNames: "custom_properties_id", baseTableName: "service", constraintName: "FKlcsx75pv26118e28ske0wgft7", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "custom_property_container")
    }

    changeSet(author: "ianibbo (generated)", id: "1548593720305-48") {
        addForeignKeyConstraint(baseColumnNames: "sa_service", baseTableName: "service_account", constraintName: "FKlw0rgy9jm8bhf9cn2ok7yr76b", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "se_id", referencedTableName: "service")
    }

    changeSet(author: "ianibbo (generated)", id: "1548593720305-49") {
        addForeignKeyConstraint(baseColumnNames: "tag_id", baseTableName: "service_tag", constraintName: "FKq56hgx6qad4r28rntiynnitg8", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "tag")
    }

    changeSet(author: "ianibbo (generated)", id: "1548593720305-50") {
        addForeignKeyConstraint(baseColumnNames: "service_tags_id", baseTableName: "service_tag", constraintName: "FKq58uyhoq6ouyw991t9aps47ka", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "se_id", referencedTableName: "service")
    }

    changeSet(author: "ianibbo (generated)", id: "1548593720305-51") {
        addForeignKeyConstraint(baseColumnNames: "tag_id", baseTableName: "directory_entry_tag", constraintName: "FKt8qbn40lvi5a2hi726uqc5igv", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "tag")
    }
}

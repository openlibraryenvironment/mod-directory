databaseChangeLog = {

    changeSet(author: "ibbo (generated)", id: "1539613647884-1") {
        createTable(tableName: "test_table") {
            column(name: "tt_id", type: "VARCHAR(36)") {
                constraints(nullable: "false")
            }

            column(name: "tt_version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "tt_name", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }
        }
    }

}

databaseChangeLog = {

    changeSet(author: "marianosimone", id: "1340301471323-1") {
        dropForeignKeyConstraint(baseTableName: "milestone", baseTableSchemaName: "project_guide", constraintName: "FKC0841970DB5D86FA")
    }

    changeSet(author: "marianosimone", id: "1340301471323-5") {
        dropTable(tableName: "milestone")
    }

}

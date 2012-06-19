databaseChangeLog = {

    changeSet(author: "marianosimone", id: "1340139736373-1") {
        dropForeignKeyConstraint(baseTableName: "client", baseTableSchemaName: "project_guide", constraintName: "FKAF12F3CBED2A3E7A")
    }

    changeSet(author: "marianosimone", id: "1340139736373-2") {
        dropForeignKeyConstraint(baseTableName: "project", baseTableSchemaName: "project_guide", constraintName: "FKED904B19ED2A3E7A")
    }

    changeSet(author: "marianosimone", id: "1340139736373-3") {
        dropForeignKeyConstraint(baseTableName: "project", baseTableSchemaName: "project_guide", constraintName: "FKED904B196F9C5FA")
    }

    changeSet(author: "marianosimone", id: "1340139736373-4") {
        dropIndex(indexName: "email", tableName: "client")
    }

    changeSet(author: "marianosimone", id: "1340139736373-5") {
        dropColumn(columnName: "account_id", tableName: "project")
    }

    changeSet(author: "marianosimone", id: "1340139736373-6") {
        dropColumn(columnName: "client_id", tableName: "project")
    }

    changeSet(author: "marianosimone", id: "1340139736373-7") {
        dropTable(tableName: "account")
    }

    changeSet(author: "marianosimone", id: "1340139736373-8") {
        dropTable(tableName: "client")
    }
}

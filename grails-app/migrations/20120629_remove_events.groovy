databaseChangeLog = {

	changeSet(author: "marianosimone", id: "1340985106914-1") {
		dropForeignKeyConstraint(baseTableName: "event", baseTableSchemaName: "project_guide", constraintName: "FK5C6729A69595C7A")
	}

	changeSet(author: "marianosimone", id: "1340985106914-2") {
		dropForeignKeyConstraint(baseTableName: "event_user", baseTableSchemaName: "project_guide", constraintName: "FK3AACC250D029ACEF")
	}

	changeSet(author: "marianosimone", id: "1340985106914-3") {
		dropForeignKeyConstraint(baseTableName: "event_user", baseTableSchemaName: "project_guide", constraintName: "FK3AACC250F7634DFA")
	}

	changeSet(author: "marianosimone", id: "1340985106914-7") {
		dropTable(tableName: "event")
	}

	changeSet(author: "marianosimone", id: "1340985106914-8") {
		dropTable(tableName: "event_user")
	}

}

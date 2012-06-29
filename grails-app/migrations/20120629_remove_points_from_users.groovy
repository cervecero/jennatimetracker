databaseChangeLog = {
	changeSet(author: "marianosimone", id: "1340984324573-8") {
		dropColumn(tableName: "user", columnName: "points")
	}
}

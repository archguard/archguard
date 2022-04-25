package org.archguard.scanner.sql.model

class SimpleRelation(
    var tableNames: List<String> = listOf(),
    val fields: HashMap<String, String> = hashMapOf()
)
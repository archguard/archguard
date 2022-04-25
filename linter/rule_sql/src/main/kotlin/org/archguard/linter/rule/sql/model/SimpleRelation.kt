package org.archguard.linter.rule.sql.model

class SimpleRelation(
    var tableNames: List<String> = listOf(),
    val fields: HashMap<String, String> = hashMapOf()
)
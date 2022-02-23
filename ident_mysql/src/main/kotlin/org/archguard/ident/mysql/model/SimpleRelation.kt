package org.archguard.ident.mysql.model

class SimpleRelation(
    var tableNames: List<String> = listOf(),
    val fields: HashMap<String, String> = hashMapOf()
)
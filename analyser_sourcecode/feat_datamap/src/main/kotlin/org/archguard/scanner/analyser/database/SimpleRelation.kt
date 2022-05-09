package org.archguard.scanner.analyser.database

class SimpleRelation(
    var tableNames: List<String> = listOf(),
    val fields: HashMap<String, String> = hashMapOf()
)
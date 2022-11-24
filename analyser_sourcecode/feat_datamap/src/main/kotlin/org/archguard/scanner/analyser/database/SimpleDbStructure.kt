package org.archguard.scanner.analyser.database

class SimpleDbStructure(
    var tableNames: List<String> = listOf(),
    val fields: HashMap<String, String> = hashMapOf()
)
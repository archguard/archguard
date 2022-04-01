package org.archguard.scanner.common

import infrastructure.SourceBatch
import org.archguard.scanner.common.database.CodeDatabaseRelation
import java.util.HashMap

class DatamapRepository(systemId: String, language: String, workspace: String) {
    private val batch: SourceBatch = SourceBatch()
    private val systemId: String
    private val language: String
    private val workspace: String

    init {
        this.systemId = systemId
        this.language = language
        this.workspace = workspace
    }

    fun saveRelations(records: List<CodeDatabaseRelation>) {
        records.forEach {
            saveRecord(it)
        }
    }

    private fun saveRecord(relation: CodeDatabaseRelation): String {
        val id = ClassRepository.generateId()
        val values: MutableMap<String, String> = HashMap()

        values["id"] = id
        values["system_id"] = systemId

        values["package_name"] = relation.packageName
        values["class_name"] = relation.className
        values["method_name"] = relation.functionName

        values["tables"] = relation.tables.joinToString(",")
        values["sqls"] = ""

        batch.add("data_code_database_relation", values)
        return id
    }

    fun close() {
        batch.execute()
        batch.close()
    }
}
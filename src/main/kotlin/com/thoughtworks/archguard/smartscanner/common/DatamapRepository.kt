package com.thoughtworks.archguard.smartscanner.common

import com.thoughtworks.archguard.infrastructure.SourceBatch
import com.thoughtworks.archguard.smartscanner.common.RepositoryHelper.generateId
import com.thoughtworks.archguard.smartscanner.dto.CodeDatabaseRelation
import java.util.concurrent.atomic.AtomicInteger

class DatamapRepository(systemId: String, language: String, workspace: String) {
    private val batch: SourceBatch =
        SourceBatch()
    private val count = AtomicInteger(0)
    private val batchStep = 100

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

            count.incrementAndGet()
            if (count.get() == batchStep) {
                flush()
                count.compareAndSet(batchStep, 0)
            }

        }
    }

    private fun saveRecord(relation: CodeDatabaseRelation): String {
        val id = generateId()
        val values: MutableMap<String, String> = HashMap()

        values["id"] = id
        values["system_id"] = systemId

        values["package_name"] = relation.packageName
        values["class_name"] = relation.className
        values["function_name"] = relation.functionName

        values["tables"] = relation.tables.joinToString(",")
        // `,` is a symbol in sql
//        values["sqls"] = relation.sqls.joinToString("\n::archguard::\n") {
//            it.replace("\'", "\'\'")
//        }
        values["sqls"] = ""

        batch.add("data_code_database_relation", values)
        return id
    }

    private fun flush() {
        batch.execute()
    }

    fun close() {
        batch.execute()
        batch.close()
    }
}

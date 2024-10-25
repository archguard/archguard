package com.thoughtworks.archguard.smartscanner.repository

import com.thoughtworks.archguard.infrastructure.SourceBatch
import com.thoughtworks.archguard.smartscanner.repository.RepositoryHelper.generateId
import org.archguard.context.CodeDatabaseRelation
import java.util.concurrent.atomic.AtomicInteger

class DatamapRepository(private val systemId: String, private val language: String, private val workspace: String) {
    private val batch: SourceBatch = SourceBatch()
    private val count = AtomicInteger(0)
    private val batchStep = 100

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

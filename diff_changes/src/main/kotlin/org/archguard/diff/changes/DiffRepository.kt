package org.archguard.diff.changes

import com.thoughtworks.archguard.infrastructure.SourceBatch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.archguard.scanner.common.RepositoryHelper.currentTime
import org.archguard.scanner.common.RepositoryHelper.generateId
import java.util.*
import java.util.concurrent.atomic.AtomicInteger

class DiffRepository(
    val systemId: String,
    val language: String,
    val since: String,
    val until: String,
    val tableName: String
) {
    private val batch: SourceBatch = SourceBatch()
    private val count = AtomicInteger(0)
    private val batchStep = 100

    fun saveDiff(calls: List<ChangedCall>) {
        calls.forEach {
            saveCall(it)

            count.incrementAndGet()
            if (count.get() == batchStep) {
                flush()
                count.compareAndSet(batchStep, 0)
            }
        }
    }

    private fun saveCall(changedCall: ChangedCall) {
        val values: MutableMap<String, String> = HashMap()

        val time = currentTime
        val id = generateId()

        values["id"] = id
        values["updated_at"] = time
        values["created_at"] = time
        values["system_id"] = systemId
        values["since_rev"] = since
        values["until_rev"] = until
        values["class_name"] = changedCall.className
        values["package_name"] = changedCall.packageName
        values["relations"] = Json.encodeToString(changedCall.relations)

        batch.add("scm_diff_change", values)
    }

    private fun flush() {
        batch.executeByTable(tableName)
    }

    fun close() {
        batch.executeByTable(tableName)
        batch.close()
    }
}

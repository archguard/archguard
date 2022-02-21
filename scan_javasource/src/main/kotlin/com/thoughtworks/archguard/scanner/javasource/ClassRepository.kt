package com.thoughtworks.archguard.scanner.javasource

import chapi.domain.core.CodeDataStruct
import infrastructure.SourceBatch
import java.text.SimpleDateFormat
import java.util.*

class ClassRepository(systemId: String) {
    private val batch: SourceBatch
    private val systemId: String

    init {
        batch = SourceBatch()
        this.systemId = systemId
    }

    fun saveClassElement(clz: CodeDataStruct) {
        saveClass(clz)
    }

    private fun saveClass(clz: CodeDataStruct) {
        val time = currentTime
        val clzId = generateId()
        val values: MutableMap<String, String> = HashMap()
        values["id"] = clzId
        values["system_id"] = systemId
        values["name"] = clz.NodeName
        values["is_thirdparty"] = "false"
        values["is_test"] = "false"
        values["updatedAt"] = time
        values["createdAt"] = time
        values["module"] = clz.Package
        values["package_name"] = clz.Package
        values["class_name"] = clz.NodeName
        values["access"] = "todo"
        batch.add("JClass", values)
    }

    companion object {
        fun generateId(): String {
            return UUID.randomUUID().toString()
        }

        val currentTime: String
            get() {
                val dt = Date()
                val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                return sdf.format(dt)
            }
    }

    fun close() {
        batch.execute()
        batch.close()
    }
}
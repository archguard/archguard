package com.thoughtworks.archguard.scanner2.infrastructure.mysql

import org.archguard.model.code.JClass
import com.thoughtworks.archguard.scanner2.domain.repository.DataClassRepository
import org.jdbi.v3.core.Jdbi
import org.springframework.stereotype.Repository

@Repository
class ScannerDataClassRepositoryImpl(val jdbi: Jdbi) : DataClassRepository {
    override fun insertOrUpdateDataClass(systemId: Long, dataClasses: List<JClass>) {
        deleteDataClass(systemId)
        dataClasses.flatMap { jclass -> jclass.fields.map { DataClassPO(jclass.id, it.id, systemId) } }.forEach {
            saveDataClass(it)
        }
    }

    fun deleteDataClass(systemId: Long) {
        jdbi.useHandle<Exception> {
            it.createUpdate("delete from metric_dataclass where system_id=:system_id")
                .bind("system_id", systemId)
                .execute()
        }
    }

    fun saveDataClass(dataClassPO: DataClassPO) {
        jdbi.useHandle<Exception> {
            it.createUpdate("insert into metric_dataclass (system_id, class_id, field_id) values (:system_id, :class_id, :field_id)")
                .bind("system_id", dataClassPO.systemId)
                .bind("class_id", dataClassPO.classId)
                .bind("field_id", dataClassPO.fieldId)
                .execute()
        }
    }
}

data class DataClassPO(val classId: String, val fieldId: String, val systemId: Long)

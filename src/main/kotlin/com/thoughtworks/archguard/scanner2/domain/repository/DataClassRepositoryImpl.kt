package com.thoughtworks.archguard.scanner2.domain.repository

import com.thoughtworks.archguard.scanner2.domain.model.JClass
import org.jdbi.v3.core.Jdbi
import org.springframework.stereotype.Repository

@Repository
class DataClassRepositoryImpl(val jdbi: Jdbi) : DataClassRepository {
    override fun insertOrUpdateDataClass(systemId: Long, dataClasses: List<JClass>) {
        deleteDataClass(systemId)
        dataClasses.flatMap { jclass -> jclass.fields.map { DataClassPO(jclass.id, it.id, systemId) } }.forEach {
            saveDataClass(it)
        }
    }

    fun deleteDataClass(systemId: Long) {
        jdbi.useHandle<Exception> {
            it.createUpdate("delete from data_class where system_id=:system_id")
                    .bind("system_id", systemId)
                    .execute()
        }
    }

    fun saveDataClass(dataClassPO: DataClassPO) {
        jdbi.useHandle<Exception> {
            it.createUpdate("insert into data_class (system_id, class_id, field_id) values (:system_id, :class_id, :field_id)")
                    .bind("system_id", dataClassPO.systemId)
                    .bind("class_id", dataClassPO.classId)
                    .bind("field_id", dataClassPO.fieldId)
                    .execute()
        }
    }
}

data class DataClassPO(val classId: String, val fieldId: String, val systemId: Long)
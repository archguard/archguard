package com.thoughtworks.archguard.datamap.infrastructure

import com.thoughtworks.archguard.datamap.domain.Datamap
import com.thoughtworks.archguard.datamap.domain.DatamapRepository
import org.jdbi.v3.core.Jdbi
import org.springframework.stereotype.Repository

@Repository
class DatamapRepositoryImpl(val jdbi: Jdbi) : DatamapRepository {
    override fun getDatamapBySystemId(systemId: Long): List<Datamap> {
        val sql = "select id, package_name as packageName, class_name as className, function_name as functionName," +
                " system_id as systemId, tables " +
                " from data_code_database_relation where system_id=:systemId"
        return jdbi.withHandle<List<Datamap>, Exception> {
            it.createQuery(sql)
                .bind("systemId", systemId)
                .mapTo(Datamap::class.java).list()
        }
    }
}
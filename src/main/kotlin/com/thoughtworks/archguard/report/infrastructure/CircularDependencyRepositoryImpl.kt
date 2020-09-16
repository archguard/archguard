package com.thoughtworks.archguard.report.infrastructure

import com.thoughtworks.archguard.report.domain.circulardependency.CircularDependencyRepository
import com.thoughtworks.archguard.report.domain.circulardependency.CircularDependencyType
import org.jdbi.v3.core.Jdbi
import org.springframework.stereotype.Repository

@Repository
class CircularDependencyRepositoryImpl(val jdbi: Jdbi) : CircularDependencyRepository {
    override fun getCircularDependency(systemId: Long, type: CircularDependencyType, limit: Long, offset: Long): List<String> {
        val sql = "select circular_dependency from circular_dependency_metrics where system_id=:system_id and type=:type order by circular_dependency LIMIT :limit OFFSET :offset"
        return jdbi.withHandle<List<String>, Exception> {
            it.createQuery(sql)
                    .bind("system_id", systemId)
                    .bind("type", type)
                    .bind("limit", limit)
                    .bind("offset", offset)
                    .mapTo(String::class.java)
                    .list()
        }
    }

    override fun getCircularDependencyCount(systemId: Long, type: CircularDependencyType): Long {
        val sql = "select count(1) from circular_dependency_metrics where system_id=:system_id and type=:type"
        return jdbi.withHandle<Long, Exception> {
            it.createQuery(sql)
                    .bind("system_id", systemId)
                    .bind("type", type)
                    .mapTo(Long::class.java)
                    .one()
        }
    }
}
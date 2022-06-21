package com.thoughtworks.archguard.insights

import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.mapper.reflect.ConstructorMapper
import org.springframework.stereotype.Repository

@Repository
class InsightRepositoryImpl(val jdbi: Jdbi) : InsightRepository {
    override fun filterByCondition(id: Long, artifact: String): Long {
        val sql =
            "select count(name) from project_composition_dependencies where system_id = :id and dep_artifact = :artifact"
        return jdbi.withHandle<Long, Nothing> {
            it.createQuery(sql)
                .bind("id", id)
                .bind("artifact", artifact)
                .mapTo(Long::class.java)
                .findOne()
                .orElse(0)
        }
    }
}

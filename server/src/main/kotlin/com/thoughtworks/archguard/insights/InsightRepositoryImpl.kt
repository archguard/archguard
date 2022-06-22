package com.thoughtworks.archguard.insights

import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.mapper.reflect.ConstructorMapper
import org.springframework.stereotype.Repository

@Repository
class InsightRepositoryImpl(val jdbi: Jdbi) : InsightRepository {
    override fun filterByCondition(id: Long): List<ScaModelDto> {
        val sql =
            "select dep_artifact, dep_group, dep_version, dep_name" +
                    " from project_composition_dependencies where system_id = :id "

        return jdbi.withHandle<List<ScaModelDto>, Nothing> {
            it.registerRowMapper(ConstructorMapper.factory(ScaModelDto::class.java))
            it.createQuery(sql)
                .bind("id", id)
                .mapTo(ScaModelDto::class.java)
                .list()
        }
    }
}

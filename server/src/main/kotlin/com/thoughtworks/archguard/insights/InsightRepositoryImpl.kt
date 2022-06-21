package com.thoughtworks.archguard.insights

import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.mapper.reflect.ConstructorMapper
import org.springframework.stereotype.Repository

@Repository
class InsightRepositoryImpl(val jdbi: Jdbi) : InsightRepository {
    override fun filterByCondition(id: Long, mappings: MutableMap<String, String>): List<ScaModelDto> {
        var sql =
            "select dep_artifact, dep_group, dep_version" +
                    " from project_composition_dependencies where system_id = :id "

        mappings.map {
            sql += " and ${it.key} = ${it.value}"
        }

        return jdbi.withHandle<List<ScaModelDto>, Nothing> {
            it.registerRowMapper(ConstructorMapper.factory(ScaModelDto::class.java))
            it.createQuery(sql)
                .bind("id", id)
                .mapTo(ScaModelDto::class.java)
                .list()
        }
    }
}

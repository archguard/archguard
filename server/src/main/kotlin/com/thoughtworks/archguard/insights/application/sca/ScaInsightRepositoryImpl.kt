package com.thoughtworks.archguard.insights.application.sca

import com.thoughtworks.archguard.insights.application.InsightModelDto
import org.archguard.domain.insight.Query
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.mapper.reflect.ConstructorMapper
import org.springframework.stereotype.Repository

@Repository
class ScaInsightRepositoryImpl(val jdbi: Jdbi) : ScaInsightRepository {
    override fun filterByConditionWithSystemId(id: Long, query: Query): List<InsightModelDto> {
        var sql =
            "select dep_artifact, dep_group, dep_version, dep_name" +
                    " from project_composition_dependencies where system_id = :id "

        sql += query.toSQL("AND")

        return jdbi.withHandle<List<InsightModelDto>, Nothing> {
            it.registerRowMapper(ConstructorMapper.factory(InsightModelDto::class.java))
            it.createQuery(sql)
                .bind("id", id)
                .mapTo(InsightModelDto::class.java)
                .list()
        }
    }

    override fun filterByCondition(query: Query): List<InsightModelDto> {
        var sql =
            "select dep_artifact, dep_group, dep_version, dep_name" +
                    " from project_composition_dependencies "

        sql += query.toSQL()

        return jdbi.withHandle<List<InsightModelDto>, Nothing> {
            it.registerRowMapper(ConstructorMapper.factory(InsightModelDto::class.java))
            it.createQuery(sql)
                .mapTo(InsightModelDto::class.java)
                .list()
        }
    }
}

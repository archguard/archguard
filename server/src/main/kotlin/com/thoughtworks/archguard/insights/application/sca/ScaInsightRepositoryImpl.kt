package com.thoughtworks.archguard.insights.application.sca

import com.thoughtworks.archguard.insights.application.InsightModelDto
import org.archguard.domain.insight.FieldFilter
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.mapper.reflect.ConstructorMapper
import org.springframework.stereotype.Repository

@Repository
class ScaInsightRepositoryImpl(val jdbi: Jdbi) : ScaInsightRepository {
    override fun filterByConditionWithSystemId(id: Long, models: List<FieldFilter>): List<InsightModelDto> {
        var sql =
            "select dep_artifact, dep_group, dep_version, dep_name" +
                    " from project_composition_dependencies where system_id = :id "

        sql += FieldFilter.toQuery(models, "and")

        return jdbi.withHandle<List<InsightModelDto>, Nothing> {
            it.registerRowMapper(ConstructorMapper.factory(InsightModelDto::class.java))
            it.createQuery(sql)
                .bind("id", id)
                .mapTo(InsightModelDto::class.java)
                .list()
        }
    }

    override fun filterByCondition(models: List<FieldFilter>): List<InsightModelDto> {
        var sql =
            "select dep_artifact, dep_group, dep_version, dep_name" +
                    " from project_composition_dependencies "

        sql += FieldFilter.toQuery(models, "where")

        return jdbi.withHandle<List<InsightModelDto>, Nothing> {
            it.registerRowMapper(ConstructorMapper.factory(InsightModelDto::class.java))
            it.createQuery(sql)
                .mapTo(InsightModelDto::class.java)
                .list()
        }
    }
}

package com.thoughtworks.archguard.insights.application

import org.archguard.domain.insight.FieldFilter
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.mapper.reflect.ConstructorMapper
import org.springframework.stereotype.Repository

@Repository
class IssueInsightRepositoryImpl(val jdbi: Jdbi) : IssueInsightRepository {
    override fun filterByConditionWithSystemId(id: Long, models: List<FieldFilter>): List<IssueModelDto> {
        var sql =
            "select name, rule_id, rule_type, severity " +
                    " from governance_issue where system_id = :id "

        val additionCondition: String = FieldFilter.toQuery(models)
        if (additionCondition.isNotEmpty()) {
            sql += additionCondition
        }

        return jdbi.withHandle<List<IssueModelDto>, Nothing> {
            it.registerRowMapper(ConstructorMapper.factory(InsightModelDto::class.java))
            it.createQuery(sql)
                .bind("id", id)
                .mapTo(IssueModelDto::class.java)
                .list()
        }
    }

    override fun filterByCondition(models: List<FieldFilter>): List<IssueModelDto> {
        var sql =
            "select name, rule_id, rule_type, severity" +
                    " from governance_issue "

        val additionCondition: String = FieldFilter.toQuery(models)
        if (additionCondition.isNotEmpty()) {
            sql += "where $additionCondition"
        }

        return jdbi.withHandle<List<IssueModelDto>, Nothing> {
            it.registerRowMapper(ConstructorMapper.factory(InsightModelDto::class.java))
            it.createQuery(sql)
                .mapTo(IssueModelDto::class.java)
                .list()
        }
    }
}

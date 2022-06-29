package com.thoughtworks.archguard.insights.application.issue

import com.thoughtworks.archguard.insights.application.InsightModelDto
import com.thoughtworks.archguard.insights.application.IssueModelDto
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

        sql += FieldFilter.toQuery(models, "and")

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

        sql += FieldFilter.toQuery(models, "where")

        return jdbi.withHandle<List<IssueModelDto>, Nothing> {
            it.registerRowMapper(ConstructorMapper.factory(InsightModelDto::class.java))
            it.createQuery(sql)
                .mapTo(IssueModelDto::class.java)
                .list()
        }
    }
}

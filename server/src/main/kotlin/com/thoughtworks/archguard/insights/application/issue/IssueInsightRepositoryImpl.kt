package com.thoughtworks.archguard.insights.application.issue

import com.thoughtworks.archguard.insights.application.InsightModelDto
import com.thoughtworks.archguard.insights.application.IssueModelDto
import org.archguard.domain.insight.Query
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.mapper.reflect.ConstructorMapper
import org.springframework.stereotype.Repository

@Repository
class IssueInsightRepositoryImpl(val jdbi: Jdbi) : IssueInsightRepository {
    override fun filterByConditionWithSystemId(id: Long, query: Query): List<IssueModelDto> {
        var sql =
            "SELECT name, rule_id, rule_type, severity " +
                    " FROM governance_issue WHERE system_id = :id "

        sql += query.toSQL("AND")

        return jdbi.withHandle<List<IssueModelDto>, Nothing> {
            it.registerRowMapper(ConstructorMapper.factory(InsightModelDto::class.java))
            it.createQuery(sql)
                .bind("id", id)
                .mapTo(IssueModelDto::class.java)
                .list()
        }
    }

    override fun filterByCondition(query: Query): List<IssueModelDto> {
        var sql =
            "SELECT name, rule_id, rule_type, severity" +
                    " FROM governance_issue "

        sql += query.toSQL()

        return jdbi.withHandle<List<IssueModelDto>, Nothing> {
            it.registerRowMapper(ConstructorMapper.factory(InsightModelDto::class.java))
            it.createQuery(sql)
                .mapTo(IssueModelDto::class.java)
                .list()
        }
    }
}

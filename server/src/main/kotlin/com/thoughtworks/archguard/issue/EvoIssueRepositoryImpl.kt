package com.thoughtworks.archguard.issue

import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.mapper.reflect.ConstructorMapper
import org.springframework.stereotype.Repository

@Repository
class EvoIssueRepositoryImpl(val jdbi: Jdbi) : EvoIssueRepository {
    override fun getAll(id: Long): List<EvoIssueModel> {
        val sql =
            "select  position, rule_id as ruleId, name, detail, rule_type as ruleType, severity, full_name as fullName, source from governance_issue where system_id = :id "
        return jdbi.withHandle<List<EvoIssueModel>, Nothing> {
            it.registerRowMapper(ConstructorMapper.factory(EvoIssueModel::class.java))
            it.createQuery(sql)
                .bind("id", id)
                .mapTo(EvoIssueModel::class.java)
                .list()
        }
    }
}

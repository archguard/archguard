package com.thoughtworks.archguard.evolution.infrastructure

import com.thoughtworks.archguard.evolution.domain.EvoIssueRepository
import com.thoughtworks.archguard.evolution.domain.IssueModel
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.mapper.reflect.ConstructorMapper
import org.springframework.stereotype.Repository

@Repository
class EvoIssueRepositoryImpl(val jdbi: Jdbi) : EvoIssueRepository {
    override fun getAll(id: Long): List<IssueModel> {
        val sql =
            "select  position, rule_id as ruleId, name, detail, rule_type as ruleType, severity, full_name as fullName, source from governance_issue where system_id = :id"
        return jdbi.withHandle<List<IssueModel>, Nothing> {
            it.registerRowMapper(ConstructorMapper.factory(IssueModel::class.java))
            it.createQuery(sql)
                .bind("id", id)
                .mapTo(IssueModel::class.java)
                .list()
        }
    }
}

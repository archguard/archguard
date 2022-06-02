package com.thoughtworks.archguard.smartscanner.repository

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.archguard.rule.core.Issue
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.sqlobject.customizer.BindBean
import org.jdbi.v3.sqlobject.statement.SqlBatch
import org.jdbi.v3.sqlobject.statement.SqlUpdate
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository


data class IssueDto(
    val position: String,
    val ruleId: String,
    val name: String,
    val detail: String,
    val ruleType: String,
    val severity: String,
    val fullName: String = "",
    val source: String = "",
) {
    companion object {
        fun from(it: Issue): IssueDto {
            return IssueDto(
                position = Json.encodeToString(it.position),
                ruleId = it.ruleId,
                name = it.name,
                detail = it.detail,
                ruleType = it.ruleType.toString(),
                severity = it.severity.toString(),
                fullName = it.fullName,
                source = it.source,
            )
        }
    }

}

@Repository
class IssueRepository(private val jdbi: Jdbi) {
    private val log = LoggerFactory.getLogger(this.javaClass)
    private val dao: IssueDao by lazy { jdbi.onDemand(IssueDao::class.java) }

    fun saveIssues(systemId: Long, input: List<IssueDto>, type: String) {
        log.debug("clean up old data for systemId: $systemId and type: $type")
        dao.deleteBySystemIdAndType(systemId, type)

        log.debug("save new data for systemId: $systemId")
        dao.saveAll(systemId, input)

        log.debug("save new data for systemId: $systemId done")
    }
}

interface IssueDao {
    @SqlBatch(
        """
        INSERT INTO governance_issue (system_id, name, rule_id, detail, rule_type, severity, full_name, source, position)
        VALUES (:systemId, :item.name, :item.ruleId, :item.detail, :item.ruleType, :item.severity, :item.fullName, :item.source, :item.position)
        """
    )
    fun saveAll(systemId: Long, @BindBean("item") issues: List<IssueDto>)

    @SqlUpdate("DELETE FROM governance_issue WHERE system_id = :systemId AND rule_type = :type")
    fun deleteBySystemIdAndType(systemId: Long, type: String)
}

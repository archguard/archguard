package com.thoughtworks.archguard.v2.backyard.smartscanner.repository

import com.fasterxml.jackson.databind.ObjectMapper
import org.archguard.scanner.core.diffchanges.ChangedCall
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.sqlobject.customizer.BindBean
import org.jdbi.v3.sqlobject.statement.SqlBatch
import org.jdbi.v3.sqlobject.statement.SqlUpdate
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository

@Repository
class DiffChangesRepository(
    private val jdbi: Jdbi,
    private val objectMapper: ObjectMapper,
) {
    private val log = LoggerFactory.getLogger(this.javaClass)
    private val dao: DiffChangesDao by lazy { jdbi.onDemand(DiffChangesDao::class.java) }

    fun saveDiffs(
        systemId: Long,
        since: String,
        until: String,
        input: List<ChangedCall>,
    ) {
        log.debug("clean up old data for systemId: $systemId")
        dao.deleteBySystemId(systemId)

        log.debug("save new data for systemId: $systemId")
        val currentTime = RepositoryHelper.getCurrentTime()
        dao.saveAll(
            input.map {
                it.run {
                    ChangedCallPo(
                        RepositoryHelper.generateId(),
                        currentTime,
                        currentTime,
                        systemId,
                        since,
                        until,
                        className,
                        packageName,
                        objectMapper.writeValueAsString(relations),
                    )
                }
            }
        )

        log.debug("save new data for systemId: $systemId done")
    }
}

data class ChangedCallPo(
    val id: String,
    val createdAt: String,
    val updatedAt: String,
    val systemId: Long,
    val since: String,
    val until: String,
    val className: String,
    val packageName: String,
    val relations: String,
)

interface DiffChangesDao {
    @SqlBatch(
        """
        INSERT INTO scm_diff_change (id,updated_at,created_at,system_id,since_rev,until_rev,class_name,package_name,relations)
        VALUES (:item.id,:item.createdAt,:item.updatedAt,:item.systemId,:item.since,:item.until,:item.className,:item.packageName,:item.relations)
        """
    )
    fun saveAll(@BindBean("item") changedCalls: List<ChangedCallPo>)

    @SqlUpdate("DELETE FROM scm_diff_change WHERE system_id = :systemId")
    fun deleteBySystemId(systemId: Long)
}

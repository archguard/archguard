package com.thoughtworks.archguard.smartscanner.repository

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.archguard.scanner.core.estimate.LanguageEstimate
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.sqlobject.customizer.BindBean
import org.jdbi.v3.sqlobject.statement.SqlBatch
import org.jdbi.v3.sqlobject.statement.SqlUpdate
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository

@Repository
class EstimateRepository(private val jdbi: Jdbi) {
    private val log = LoggerFactory.getLogger(this.javaClass)
    private val dao: EstimateDao by lazy { jdbi.onDemand(EstimateDao::class.java) }

    fun save(systemId: Long, input: List<LanguageEstimate>) {
        log.debug("clean up old data for systemId: $systemId")
        dao.deleteBySystemId(systemId)

        log.debug("save new data for systemId: $systemId")
        dao.saveAll(systemId, input)

        log.debug("save new data for systemId: $systemId done")
    }
}

class EstimateDto {

}

interface EstimateDao {
    @SqlBatch(
        """
        INSERT INTO metric_estimate (system_id, cost, month, people, name, files, `lines`, blanks, comment, code, complexity)
        VALUES (:systemId, :item.cost, :item.month, :item.people, :item.name, :item.files, :item.lines, :item.blanks, :item.comment, :item.code, :item.complexity)
        ON DUPLICATE KEY UPDATE id=id
        """
    )
    fun saveAll(systemId: Long, @BindBean("item") languageEstimates: List<LanguageEstimate>)

    @SqlUpdate("DELETE FROM metric_estimate WHERE system_id = :systemId")
    fun deleteBySystemId(systemId: Long)
}

package com.thoughtworks.archguard.evolution.infrastructure

import com.thoughtworks.archguard.report.domain.badsmell.ThresholdSuite
import com.thoughtworks.archguard.report.domain.badsmell.ThresholdSuiteRepository
import com.thoughtworks.archguard.report.infrastructure.BadSmellSuitePO
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.mapper.reflect.ConstructorMapper
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository

@Repository
class ThresholdSuiteRepositoryImpl(val jdbi: Jdbi) : ThresholdSuiteRepository {

    private val logger = LoggerFactory.getLogger(ThresholdSuiteRepositoryImpl::class.java)

    override fun getAllBadSmellThresholdSuites(): List<ThresholdSuite> {
        val sql = "select a.id, a.suite_name, a.thresholds, GROUP_CONCAT(a.systemId SEPARATOR ', ') as systemIds from " +
            "(select bs.id, bs.suite_name, bs.thresholds, si.id as systemId " +
            "from metric_bad_smell_threshold_suite bs, system_info si where bs.id = si.threshold_suite_id) as a " +
            "group by a.id;"

//        logger.info("ThresholdSuiteRepositoryImpl: $sql")
        return jdbi.withHandle<List<BadSmellSuitePO>, Nothing> {
            it.registerRowMapper(ConstructorMapper.factory(BadSmellSuitePO::class.java))
            it.createQuery(sql)
                .mapTo(BadSmellSuitePO::class.java)
                .list()
        }.map { it.toBadSmellSuite() }
    }
}

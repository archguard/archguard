package com.thoughtworks.archguard.evolution.infrastructure

import com.thoughtworks.archguard.evolution.domain.BadSmellSuite
import com.thoughtworks.archguard.evolution.domain.BadSmellSuiteRepository
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.mapper.reflect.ConstructorMapper
import org.springframework.stereotype.Repository

@Repository
class BadSmellSuiteRepositoryImpl(val jdbi: Jdbi) : BadSmellSuiteRepository {

    override fun getAllBadSmellThresholdSuites(): List<BadSmellSuite> {
        val sql = "select id, suite_name as suiteName, is_default as isDefault, thresholds from bad_smell_threshold_suite"
        return jdbi.withHandle<List<BadSmellSuitePO>, Nothing> {
            it.registerRowMapper(ConstructorMapper.factory(BadSmellSuitePO::class.java))
            it.createQuery(sql)
                    .mapTo(BadSmellSuitePO::class.java)
                    .list()
        }.map { it.toBadSmellSuite() }
    }

    override fun getSelectedBadSmellSuiteIdBySystem(systemId: Long): Long {
        val sql = "select threshold_suite_id from system_info where id=:systemId"
        return jdbi.withHandle<Long, Nothing> {
            it.createQuery(sql)
                    .bind("systemId", systemId)
                    .mapTo(Long::class.java).one()
        }
    }

}
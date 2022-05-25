package com.thoughtworks.archguard.report.infrastructure.coupling

import com.thoughtworks.archguard.report.domain.coupling.circulardependency.CircularDependencyRepository
import com.thoughtworks.archguard.report.domain.coupling.circulardependency.CircularDependencyType
import com.thoughtworks.archguard.report.domain.overview.calculator.BadSmellCalculateResult
import org.jdbi.v3.core.Jdbi
import org.springframework.stereotype.Repository

@Repository
class CircularDependencyRepositoryImpl(val jdbi: Jdbi) : CircularDependencyRepository {
    override fun getCircularDependency(systemId: Long, type: CircularDependencyType, limit: Long, offset: Long): List<String> {
        val sql = "select circular_dependency from metric_circular_dependency where system_id=:system_id and type=:type order by circular_dependency LIMIT :limit OFFSET :offset"
        return jdbi.withHandle<List<String>, Exception> {
            it.createQuery(sql)
                .bind("system_id", systemId)
                .bind("type", type)
                .bind("limit", limit)
                .bind("offset", offset)
                .mapTo(String::class.java)
                .list()
        }
    }

    override fun getCircularDependencyCount(systemId: Long, type: CircularDependencyType): Long {
        val sql = "select count(1) from metric_circular_dependency where system_id=:system_id and type=:type"
        return jdbi.withHandle<Long, Exception> {
            it.createQuery(sql)
                .bind("system_id", systemId)
                .bind("type", type)
                .mapTo(Long::class.java)
                .one()
        }
    }

    override fun getCircularDependencyBadSmellCalculateResult(systemId: Long, type: CircularDependencyType, thresholdRanges: Array<LongRange>): BadSmellCalculateResult {
        return jdbi.withHandle<BadSmellCalculateResult, Exception> {
            val sql = """
                select (CASE when c.cd >= :level1Start and c.cd < :level1End then c.cd else 0 end) AS 'level1',
                       (CASE when c.cd >= :level2Start and c.cd < :level2End then c.cd else 0 end) AS 'level2',
                       (CASE when c.cd > :level3Start then c.cd else 0 end)                        AS 'level3'
                from (
                         select count(1) cd
                         from metric_circular_dependency
                         where system_id = :systemId
                           and type = :type
                     ) as c
            """.trimIndent()
            it.createQuery(sql)
                .bind("systemId", systemId)
                .bind("type", type)
                .bind("level1Start", thresholdRanges[0].first)
                .bind("level1End", thresholdRanges[0].last)
                .bind("level2Start", thresholdRanges[1].first)
                .bind("level2End", thresholdRanges[0].last)
                .bind("level3Start", thresholdRanges[2].first)
                .mapTo(BadSmellCalculateResult::class.java)
                .one()
        }
    }
}

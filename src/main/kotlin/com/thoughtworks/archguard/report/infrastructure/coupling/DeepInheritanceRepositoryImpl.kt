package com.thoughtworks.archguard.report.infrastructure.coupling

import com.thoughtworks.archguard.report.domain.coupling.deepinheritance.DeepInheritance
import com.thoughtworks.archguard.report.domain.coupling.deepinheritance.DeepInheritanceRepository
import com.thoughtworks.archguard.report.domain.overview.calculator.BadSmellCalculateResult
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.mapper.reflect.ConstructorMapper
import org.springframework.stereotype.Repository

@Repository
class DeepInheritanceRepositoryImpl(val jdbi: Jdbi) : DeepInheritanceRepository {

    override fun getDitAboveThresholdCount(systemId: Long, threshold: Int): Long {
        return jdbi.withHandle<Long, Exception> {
            val sql = "select count(c.id) from JClass c inner join class_metrics m on m.class_id = c.id " +
                    "where c.system_id =:system_id and m.dit > :dit"
            it.createQuery(sql)
                    .bind("system_id", systemId)
                    .bind("dit", threshold)
                    .mapTo(Long::class.java)
                    .one()
        }
    }

    override fun getDitAboveBadSmellCalculateResult(systemId: Long, thresholdRanges: Array<LongRange>): BadSmellCalculateResult {
        return jdbi.withHandle<BadSmellCalculateResult, Exception> {
            val sql = """
                select sum(CASE when c.dit >= :level1Start and c.dit < :level1End then 1 else 0 end) AS 'level1',
                       sum(CASE when c.dit >= :level2Start and c.dit < :level2End then 1 else 0 end) AS 'level2',
                       sum(CASE when c.dit > :level3Start then 1 else 0 end)                         AS 'level3'
                from (
                         select dit
                         from JClass c
                                  inner join class_metrics m on m.class_id = c.id
                         where c.system_id = :systemId) as c
            """.trimIndent()
            it.createQuery(sql)
                    .bind("systemId", systemId)
                    .bind("level1Start", thresholdRanges[0].first)
                    .bind("level1End", thresholdRanges[0].last)
                    .bind("level2Start", thresholdRanges[1].first)
                    .bind("level2End", thresholdRanges[0].last)
                    .bind("level3Start", thresholdRanges[2].first)
                    .mapTo(BadSmellCalculateResult::class.java)
                    .one()
        }
    }

    override fun getDitAboveThresholdList(systemId: Long, threshold: Int, limit: Long, offset: Long): List<DeepInheritance> {
        val sql = "select c.id, c.system_id, c.name, c.module, m.dit from JClass c " +
                "inner join class_metrics m on m.class_id = c.id " +
                "where c.system_id =:system_id and m.dit > :dit " +
                "order by m.dit desc LIMIT :limit OFFSET :offset"
        val classWithLCOM4List = jdbi.withHandle<List<ClassWithDitPO>, Exception> {
            it.registerRowMapper(ConstructorMapper.factory(ClassWithDitPO::class.java))
            it.createQuery(sql)
                    .bind("system_id", systemId)
                    .bind("dit", threshold)
                    .bind("limit", limit)
                    .bind("offset", offset)
                    .mapTo(ClassWithDitPO::class.java)
                    .list()
        }
        return classWithLCOM4List.map { it.toDeepInheritance() }
    }
}

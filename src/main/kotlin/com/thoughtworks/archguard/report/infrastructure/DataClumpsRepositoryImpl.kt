package com.thoughtworks.archguard.report.infrastructure

import com.thoughtworks.archguard.report.domain.dataclumps.ClassDataClump
import com.thoughtworks.archguard.report.domain.dataclumps.DataClumpsRepository
import com.thoughtworks.archguard.report.domain.overview.calculator.BadSmellCalculateResult
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.mapper.reflect.ConstructorMapper
import org.springframework.stereotype.Repository

@Repository
class DataClumpsRepositoryImpl(val jdbi: Jdbi) : DataClumpsRepository {

    override fun getLCOM4AboveThresholdCount(systemId: Long, threshold: Int): Long {
        return jdbi.withHandle<Long, Exception> {
            val sql = "select count(c.id) from JClass c inner join class_metrics m on m.class_id = c.id " +
                    "where c.system_id =:system_id and m.lcom4 > :lcom4"
            it.createQuery(sql)
                    .bind("system_id", systemId)
                    .bind("lcom4", threshold)
                    .mapTo(Long::class.java)
                    .one()
        }
    }

    override fun getLCOM4AboveBadSmellCalculateResult(systemId: Long, thresholdRanges: Array<LongRange>): BadSmellCalculateResult {
        return jdbi.withHandle<BadSmellCalculateResult, Exception> {
            val sql = """
                select sum(CASE when c.lcom4 >= :level1Start and c.lcom4 < :level1End then 1 else 0 end) AS 'level1',
                       sum(CASE when c.lcom4 >= :level2Start and c.lcom4 < :level2End then 1 else 0 end) AS 'level2',
                       sum(CASE when c.lcom4 > :level3Start then 1 else 0 end)                           AS 'level3'
                from (
                         select lcom4
                         from JClass c
                                  inner join class_metrics m on m.class_id = c.id
                         where c.system_id = :systemId
                     ) as c
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

    override fun getLCOM4AboveThresholdList(systemId: Long, threshold: Int, limit: Long, offset: Long): List<ClassDataClump> {
        val sql = "select c.id, c.system_id, c.name, c.module, m.lcom4 from JClass c " +
                "inner join class_metrics m on m.class_id = c.id " +
                "where c.system_id =:system_id and m.lcom4 > :lcom4 " +
                "order by m.lcom4 desc LIMIT :limit OFFSET :offset"
        val classWithLCOM4List = jdbi.withHandle<List<ClassWithLCOM4PO>, Exception> {
            it.registerRowMapper(ConstructorMapper.factory(ClassWithLCOM4PO::class.java))
            it.createQuery(sql)
                    .bind("system_id", systemId)
                    .bind("lcom4", threshold)
                    .bind("limit", limit)
                    .bind("offset", offset)
                    .mapTo(ClassWithLCOM4PO::class.java)
                    .list()
        }
        return classWithLCOM4List.map { it.toClassDataClump() }
    }
}

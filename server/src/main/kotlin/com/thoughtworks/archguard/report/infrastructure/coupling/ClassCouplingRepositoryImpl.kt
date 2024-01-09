package com.thoughtworks.archguard.report.infrastructure.coupling

import com.thoughtworks.archguard.report.domain.coupling.hub.ClassCoupling
import com.thoughtworks.archguard.report.domain.coupling.hub.ClassCouplingRepository
import org.archguard.smell.BadSmellCalculateResult
import org.jdbi.v3.core.Jdbi
import org.springframework.stereotype.Repository

@Repository
class ClassCouplingRepositoryImpl(val jdbi: Jdbi) : ClassCouplingRepository {
    override fun getCouplingAboveThreshold(systemId: Long, classFanInThreshold: Int, classFanOutThreshold: Int, offset: Long, limit: Long, orderByFanIn: Boolean): List<ClassCoupling> {
        var orderSqlPiece = "order by fanIn desc, fanOut desc "
        if (!orderByFanIn) {
            orderSqlPiece = "order by fanOut desc, fanIn desc "
        }
        return jdbi.withHandle<List<ClassCoupling>, Exception> {
            val sql = "select jc.id as id, jc.module as moduleName, jc.name as classFullName, " +
                "cm.fanin as fanIn, cm.fanout as fanOut from metric_class cm JOIN code_class jc " +
                "on cm.system_id = jc.system_id and cm.class_id = jc.id where cm.system_id=:systemId and " +
                "(cm.fanin > :classFanInThreshold or cm.fanout > :classFanOutThreshold) " +
                orderSqlPiece + ", moduleName, classFullName limit :limit offset :offset"
            it.createQuery(sql)
                .bind("systemId", systemId)
                .bind("offset", offset)
                .bind("limit", limit)
                .bind("classFanInThreshold", classFanInThreshold)
                .bind("classFanOutThreshold", classFanOutThreshold)
                .mapTo(ClassCouplingPO::class.java).list().map { it.toClassCoupling() }
        }
    }

    override fun getCouplingAboveThresholdCount(systemId: Long, classFanInThreshold: Int, classFanOutThreshold: Int): Long {
        return jdbi.withHandle<Long, Exception> {
            val sql = "select count(1)" +
                "from metric_class cm " +
                "JOIN code_class jc on cm.system_id = jc.system_id and cm.class_id = jc.id where cm.system_id=:systemId " +
                "and (cm.fanin > :classFanInThreshold or cm.fanout > :classFanOutThreshold)"
            it.createQuery(sql)
                .bind("systemId", systemId)
                .bind("classFanInThreshold", classFanInThreshold)
                .bind("classFanOutThreshold", classFanOutThreshold)
                .mapTo(Long::class.java).one()
        }
    }

    override fun getCouplingAboveBadSmellCalculateResult(systemId: Long, thresholdRanges: Array<LongRange>): BadSmellCalculateResult {
        return jdbi.withHandle<BadSmellCalculateResult, Exception> {
            val sql = """
                select sum(CASE
                               when (c.fanin >= :level1Start and c.fanin < :level1End
                                   or c.fanout >= :level1Start and c.fanout < :level1End) then 1
                               else 0 end) AS 'level1',
                       sum(CASE
                               when (c.fanin >= :level2Start and c.fanin < :level2End or
                                     c.fanout >= :level2Start and c.fanout < :level2End) then 1
                               else 0 end) AS 'level2',
                       sum(CASE
                               when c.fanin > :level3Start or c.fanout > :level3Start
                                   then 1
                               else 0 end) AS 'level3'
                from (
                         select fanin, fanout
                         from metric_class
                         where system_id = :systemId
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

    override fun getAllCoupling(systemId: Long): List<ClassCoupling> {
        return jdbi.withHandle<List<ClassCoupling>, Exception> {
            val sql = "select jc.id as id, jc.module as moduleName, jc.name as classFullName, " +
                "cm.fanin as fanIn, cm.fanout as fanOut " +
                "from metric_class cm " +
                "JOIN code_class jc on cm.system_id = jc.system_id and cm.class_id = jc.id where cm.system_id=:systemId " +
                "order by fanIn desc, fanOut desc, moduleName, classFullName"
            it.createQuery(sql)
                .bind("systemId", systemId)
                .mapTo(ClassCouplingPO::class.java).list().map { it.toClassCoupling() }
        }
    }
}

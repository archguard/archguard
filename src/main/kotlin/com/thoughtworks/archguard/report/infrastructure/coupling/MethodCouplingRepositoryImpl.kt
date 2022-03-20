package com.thoughtworks.archguard.report.infrastructure.coupling

import com.thoughtworks.archguard.report.domain.coupling.hub.MethodCoupling
import com.thoughtworks.archguard.report.domain.coupling.hub.MethodCouplingRepository
import com.thoughtworks.archguard.report.domain.overview.calculator.BadSmellCalculateResult
import org.jdbi.v3.core.Jdbi
import org.springframework.stereotype.Repository

@Repository
class MethodCouplingRepositoryImpl(val jdbi: Jdbi) : MethodCouplingRepository {
    override fun getCouplingAboveThreshold(systemId: Long, methodFanInThreshold: Int, methodFanOutThreshold: Int, offset: Long, limit: Long, orderByFanIn: Boolean): List<MethodCoupling> {
        var orderSqlPiece = "order by fanIn desc, fanOut desc "
        if (!orderByFanIn) {
            orderSqlPiece = "order by fanOut desc, fanIn desc "
        }
        return jdbi.withHandle<List<MethodCoupling>, Exception> {
            val sql = "select jm.id as id, jm.module as moduleName, jm.clzname as classFullName, jm.name as methodName, jm.argumenttypes as args, " +
                    "mm.fanin as fanIn, mm.fanout as fanOut from method_metrics mm JOIN code_method jm " +
                    "on mm.system_id = jm.system_id and mm.method_id = jm.id where mm.system_id=:systemId and " +
                    "(mm.fanin > :methodFanInThreshold or mm.fanout > :methodFanOutThreshold) " +
                    orderSqlPiece + ", moduleName, classFullName limit :limit offset :offset"
            it.createQuery(sql)
                    .bind("systemId", systemId)
                    .bind("offset", offset)
                    .bind("limit", limit)
                    .bind("methodFanInThreshold", methodFanInThreshold)
                    .bind("methodFanOutThreshold", methodFanOutThreshold)
                    .mapTo(MethodCouplingPO::class.java).list().map { it.toMethodCoupling() }
        }
    }

    override fun getCouplingAboveThresholdCount(systemId: Long, methodFanInThreshold: Int, methodFanOutThreshold: Int): Long {
        return jdbi.withHandle<Long, Exception> {
            val sql = "select count(1) from method_metrics mm JOIN code_method jm " +
                    "on mm.system_id = jm.system_id and mm.method_id = jm.id where mm.system_id=:systemId and " +
                    "(mm.fanin > :methodFanInThreshold or mm.fanout > :methodFanOutThreshold)"
            it.createQuery(sql)
                    .bind("systemId", systemId)
                    .bind("methodFanInThreshold", methodFanInThreshold)
                    .bind("methodFanOutThreshold", methodFanOutThreshold)
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
                         from method_metrics 
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

    override fun getAllCoupling(systemId: Long): List<MethodCoupling> {
        return jdbi.withHandle<List<MethodCoupling>, Exception> {
            val sql = "select jm.id as id, jm.module as moduleName, jm.clzname as classFullName, jm.name as methodName, jm.argumenttypes as args, " +
                    "mm.fanin as fanIn, mm.fanout as fanOut from method_metrics mm JOIN code_method jm " +
                    "on mm.system_id = jm.system_id and mm.method_id = jm.id where mm.system_id=:systemId and " +
                    "order by fanIn desc, fanOut desc, moduleName, classFullName"
            it.createQuery(sql)
                    .bind("systemId", systemId)
                    .mapTo(MethodCouplingPO::class.java).list().map { it.toMethodCoupling() }
        }
    }
}
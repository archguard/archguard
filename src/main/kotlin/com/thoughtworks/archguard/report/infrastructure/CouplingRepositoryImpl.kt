package com.thoughtworks.archguard.report.infrastructure

import com.thoughtworks.archguard.report.domain.coupling.ClassCoupling
import com.thoughtworks.archguard.report.domain.coupling.CouplingRepository
import com.thoughtworks.archguard.report.domain.overview.calculator.BadSmellCalculateResult
import org.jdbi.v3.core.Jdbi
import org.springframework.stereotype.Repository

@Repository
class CouplingRepositoryImpl(val jdbi: Jdbi) : CouplingRepository {
    override fun getCouplingAboveThreshold(systemId: Long, classFanInThreshold: Int, classFanOutThreshold: Int, offset: Long, limit: Long, orderByFanIn: Boolean): List<ClassCoupling> {
        var orderSqlPiece = "order by fanIn desc, fanOut desc "
        if (!orderByFanIn) {
            orderSqlPiece = "order by fanOut desc, fanIn desc "
        }
        return jdbi.withHandle<List<ClassCoupling>, Exception> {
            val sql = "select jc.id as id, jc.module as moduleName, jc.name as classFullName, " +
                    "(cc.inner_fan_in + cc.outer_fan_in) as fanIn, (cc.inner_fan_out + cc.outer_fan_out) as fanOut " +
                    "from class_coupling cc " +
                    "JOIN JClass jc on cc.system_id = jc.system_id and cc.class_id = jc.id where cc.system_id=:systemId " +
                    "and ((cc.inner_fan_in + cc.outer_fan_in)> :classFanInThreshold or (cc.inner_fan_out + cc.outer_fan_out) > :classFanOutThreshold)" +
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
                    "from class_coupling cc " +
                    "JOIN JClass jc on cc.system_id = jc.system_id and cc.class_id = jc.id where cc.system_id=:systemId " +
                    "and ((cc.inner_fan_in + cc.outer_fan_in)> :classFanInThreshold or (cc.inner_fan_out + cc.outer_fan_out) > :classFanOutThreshold)"
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
                         select (cc.inner_fan_in + cc.outer_fan_in) fanin, (cc.inner_fan_out + cc.outer_fan_out) fanout
                         from class_coupling cc
                                  JOIN JClass jc on cc.system_id = jc.system_id and cc.class_id = jc.id
                         where cc.system_id = :systemId
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
                    "(cc.inner_fan_in + cc.outer_fan_in) as fanIn, (cc.inner_fan_out + cc.outer_fan_out) as fanOut " +
                    "from class_coupling cc " +
                    "JOIN JClass jc on cc.system_id = jc.system_id and cc.class_id = jc.id where cc.system_id=:systemId " +
                    "order by fanIn desc, fanOut desc, moduleName, classFullName"
            it.createQuery(sql)
                    .bind("systemId", systemId)
                    .mapTo(ClassCouplingPO::class.java).list().map { it.toClassCoupling() }
        }
    }
}

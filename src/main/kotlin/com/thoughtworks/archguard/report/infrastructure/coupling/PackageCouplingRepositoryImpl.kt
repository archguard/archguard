package com.thoughtworks.archguard.report.infrastructure.coupling

import com.thoughtworks.archguard.report.domain.coupling.hub.PackageCoupling
import com.thoughtworks.archguard.report.domain.coupling.hub.PackageCouplingRepository
import com.thoughtworks.archguard.report.domain.overview.calculator.BadSmellCalculateResult
import org.jdbi.v3.core.Jdbi
import org.springframework.stereotype.Repository

@Repository
class PackageCouplingRepositoryImpl(val jdbi: Jdbi) : PackageCouplingRepository {
    override fun getCouplingAboveThreshold(systemId: Long, packageFanInThreshold: Int, packageFanOutThreshold: Int, offset: Long, limit: Long, orderByFanIn: Boolean): List<PackageCoupling> {
        var orderSqlPiece = "order by fanIn desc, fanOut desc "
        if (!orderByFanIn) {
            orderSqlPiece = "order by fanOut desc, fanIn desc "
        }
        return jdbi.withHandle<List<PackageCoupling>, Exception> {
            val sql = "select id, module_name as moduleName, package_name as packageName, fanin as fanIn, fanout as fanOut from metric_package where system_id = :systemId and " +
                    "(fanin > :packageFanInThreshold or fanout > :packageFanOutThreshold) " +
                    orderSqlPiece + ", moduleName limit :limit offset :offset"
            it.createQuery(sql)
                    .bind("systemId", systemId)
                    .bind("offset", offset)
                    .bind("limit", limit)
                    .bind("packageFanInThreshold", packageFanInThreshold)
                    .bind("packageFanOutThreshold", packageFanOutThreshold)
                    .mapTo(PackageCouplingPO::class.java).list().map { it.toPackageCoupling() }
        }

    }

    override fun getCouplingAboveThresholdCount(systemId: Long, packageFanInThreshold: Int, packageFanOutThreshold: Int): Long {
        return jdbi.withHandle<Long, Exception> {
            val sql = "select count(1) from metric_package where system_id = :systemId and (fanin > :packageFanInThreshold or fanout > :packageFanOutThreshold) "
            it.createQuery(sql)
                    .bind("systemId", systemId)
                    .bind("packageFanInThreshold", packageFanInThreshold)
                    .bind("packageFanOutThreshold", packageFanOutThreshold)
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
                         select fanin, fanout from metric_package where system_id = :systemId
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

    override fun getAllCoupling(systemId: Long): List<PackageCoupling> {
        return jdbi.withHandle<List<PackageCoupling>, Exception> {
            val sql = "select id, module_name as moduleName, package_name as packageName, fanin as fanIn, fanout as fanOut from metric_package where system_id = :systemId order by fanIn desc, fanOut desc, moduleName"
            it.createQuery(sql)
                    .bind("systemId", systemId)
                    .mapTo(PackageCouplingPO::class.java).list().map { it.toPackageCoupling() }
        }

    }
}
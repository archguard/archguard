package bugfixes

import com.thoughtworks.archguard.report.domain.coupling.hub.ModuleCoupling
import com.thoughtworks.archguard.report.domain.coupling.hub.ModuleCouplingRepository
import com.thoughtworks.archguard.report.domain.overview.calculator.BadSmellCalculateResult
import org.jdbi.v3.core.Jdbi
import org.springframework.stereotype.Repository

@Repository
class MethodCallNodeNameError(val jdbi: Jdbi) : ModuleCouplingRepository {
    override fun getCouplingAboveThreshold(systemId: Long, moduleFanInThreshold: Int, moduleFanOutThreshold: Int, offset: Long, limit: Long, orderByFanIn: Boolean): List<ModuleCoupling> {
        var orderSqlPiece = "order by fanIn desc, fanOut desc "
        if (!orderByFanIn) {
            orderSqlPiece = "order by fanOut desc, fanIn desc "
        }
        return jdbi.withHandle<List<ModuleCoupling>, Exception> {
            val sql = "select id, module_name as moduleName, fanin as fanIn, fanout as fanOut from metric_module where system_id = :systemId and " +
                "(fanin > :moduleFanInThreshold or fanout > :moduleFanOutThreshold) " +
                orderSqlPiece + ", moduleName limit :limit offset :offset"
            it.createQuery(sql)
                .bind("systemId", systemId)
                .bind("offset", offset)
                .bind("limit", limit)
                .bind("moduleFanInThreshold", moduleFanInThreshold)
                .bind("moduleFanOutThreshold", moduleFanOutThreshold)
                .mapTo(ModuleCouplingPO::class.java).list().map { it.toModuleCoupling() }
        }
    }

    override fun getCouplingAboveThresholdCount(systemId: Long, moduleFanInThreshold: Int, moduleFanOutThreshold: Int): Long {
        return jdbi.withHandle<Long, Exception> {
            val sql = "select count(1) from metric_module where system_id = :systemId and (fanin > :moduleFanInThreshold or fanout > :moduleFanOutThreshold) "
            it.createQuery(sql)
                .bind("systemId", systemId)
                .bind("moduleFanInThreshold", moduleFanInThreshold)
                .bind("moduleFanOutThreshold", moduleFanOutThreshold)
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
                         select fanin, fanout from metric_module where system_id = :systemId
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

    override fun getAllCoupling(systemId: Long): List<ModuleCoupling> {
        return jdbi.withHandle<List<ModuleCoupling>, Exception> {
            val sql = "select id, module_name as moduleName, fanin as fanIn, fanout as fanOut from metric_module where system_id = :systemId order by fanIn desc, fanOut desc, moduleName"
            it.createQuery(sql)
                .bind("systemId", systemId)
                .mapTo(ModuleCouplingPO::class.java).list().map { it.toModuleCoupling() }
        }
    }
}

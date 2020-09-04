package com.thoughtworks.archguard.report.infrastructure

import com.thoughtworks.archguard.report.domain.model.ClassHub
import com.thoughtworks.archguard.report.domain.repository.HubRepository
import org.jdbi.v3.core.Jdbi
import org.springframework.stereotype.Repository

@Repository
class HubRepositoryImpl(val jdbi: Jdbi) : HubRepository {
    override fun getClassAboveHubThresholdCount(systemId: Long, classFanInThreshold: Int, classFanOutThreshold: Int): Long {
        return jdbi.withHandle<Long, Exception> {
            val sql = "select count(1) " +
                    "from ClassStatistic c1 " +
                    "where c1.createAt = (SELECT MAX(c2.createAt) FROM ClassStatistic c2 WHERE c2.systemId = :systemId) " +
                    "  and c1.systemId = :systemId " +
                    "  and c1.fanin > :classFanInThreshold or c1.fanout>:classFanOutThreshold"
            it.createQuery(sql)
                    .bind("systemId", systemId)
                    .bind("classFanInThreshold", classFanInThreshold)
                    .bind("classFanOutThreshold", classFanOutThreshold)
                    .mapTo(Long::class.java)
                    .one()
        }
    }

    override fun getClassListAboveHubThreshold(systemId: Long, classFanInThreshold: Int, classFanOutThreshold: Int, limit: Long, offset: Long, orderByFanIn: Boolean): List<ClassHub> {
        var orderSqlPiece = "order by c1.fanin desc, c1.fanout desc "
        if (!orderByFanIn) {
            orderSqlPiece = "order by c1.fanout desc, c1.fanin desc "
        }
        return jdbi.withHandle<List<ClassHub>, Exception> {
            val sql = "select c1.systemId, c1.moduleName, c1.packageName, c1.typeName, c1.fanin, c1.fanout " +
                    "from ClassStatistic c1 " +
                    "where c1.createAt = (SELECT MAX(c2.createAt) FROM ClassStatistic c2 WHERE c2.systemId = :systemId) " +
                    "and c1.systemId = :systemId " +
                    "and c1.fanin > :classFanInThreshold or c1.fanout>:classFanOutThreshold " +
                    orderSqlPiece +
                    "limit :limit offset :offset"
            it.createQuery(sql)
                    .bind("systemId", systemId)
                    .bind("classFanInThreshold", classFanInThreshold)
                    .bind("classFanOutThreshold", classFanOutThreshold)
                    .bind("limit", limit)
                    .bind("offset", offset)
                    .mapTo(ClassHub::class.java).list()
        }
    }
}
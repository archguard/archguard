package com.thoughtworks.archguard.report.infrastructure

import com.thoughtworks.archguard.report.domain.model.MethodLine
import com.thoughtworks.archguard.report.domain.repository.CodeLineRepository
import org.jdbi.v3.core.Jdbi
import org.springframework.stereotype.Repository


@Repository
class CodeLineRepositoryImpl(val jdbi: Jdbi) : CodeLineRepository {
    override fun getMethodLinesAboveThreshold(systemId: Long, threshold: Int, limit: Long, offset: Long): List<MethodLine> {
        return jdbi.withHandle<List<MethodLine>, Exception> {
            val sql = "select m1.systemId, m1.moduleName, m1.packageName, m1.typeName, m1.methodName, m1.`lines` from MethodStatistic m1 " +
                    "where m1.createAt = (SELECT MAX(m2.createAt) FROM MethodStatistic m2 WHERE m2.systemId = :systemId) " +
                    "and m1.systemId = :systemId and m1.`lines`>:threshold order by m1.`lines` desc limit :limit offset :offset"
            it.createQuery(sql)
                    .bind("systemId", systemId)
                    .bind("threshold", threshold)
                    .bind("limit", limit)
                    .bind("offset", offset)
                    .mapTo(MethodLine::class.java).list()
        }
    }

    override fun getMethodLinesAboveThresholdCount(systemId: Long, threshold: Int): Long {
        return jdbi.withHandle<Long, Exception> {
            val sql = "select count(1) from MethodStatistic where systemId = :systemId and `lines`>:threshold"
            it.createQuery(sql)
                    .bind("systemId", systemId)
                    .bind("threshold", threshold)
                    .mapTo(Long::class.java)
                    .one()
        }
    }
}
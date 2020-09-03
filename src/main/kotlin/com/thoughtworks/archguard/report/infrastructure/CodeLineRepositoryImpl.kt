package com.thoughtworks.archguard.report.infrastructure

import com.thoughtworks.archguard.report.domain.model.MethodLine
import com.thoughtworks.archguard.report.domain.repository.CodeLineRepository
import org.jdbi.v3.core.Jdbi
import org.springframework.stereotype.Repository


@Repository
class CodeLineRepositoryImpl(val jdbi: Jdbi) : CodeLineRepository {
    override fun getMethodLinesAboveThreshold(systemId: Long, threshold: Int, limit: Long, offset: Long): List<MethodLine> {
        return jdbi.withHandle<List<MethodLine>, Exception> {
            val sql = "select systemId, moduleName, packageName, typeName, methodName, `lines` from MethodStatistic where systemId = :systemId and `lines`>:threshold order by `lines` desc limit :limit offset :offset"
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
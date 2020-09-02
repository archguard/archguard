package com.thoughtworks.archguard.report.infrastructure

import com.thoughtworks.archguard.report.domain.model.MethodLine
import com.thoughtworks.archguard.report.domain.repository.CodeLineRepository
import org.jdbi.v3.core.Jdbi
import org.springframework.stereotype.Repository


@Repository
class CodeLineRepositoryImpl(val jdbi: Jdbi) : CodeLineRepository {
    override fun getMethodLinesAboveThreshold(systemId: Long, threshold: Int): List<MethodLine> {
        return jdbi.withHandle<List<MethodLine>, Exception> {
            val sql = "select systemId, moduleName, packageName, typeName, methodName, `lines` from MethodStatistic where systemId = :systemId and `lines`>:threshold"
            it.createQuery(sql)
                    .bind("systemId", systemId)
                    .bind("threshold", threshold)
                    .mapTo(MethodLine::class.java).list()
        }
    }
}
package com.thoughtworks.archguard.report.infrastructure

import com.thoughtworks.archguard.report.domain.model.ClassSizing
import com.thoughtworks.archguard.report.domain.model.MethodSizing
import com.thoughtworks.archguard.report.domain.repository.SizingRepository
import org.jdbi.v3.core.Jdbi
import org.springframework.stereotype.Repository


@Repository
class SizingRepositoryImpl(val jdbi: Jdbi) : SizingRepository {
    override fun getMethodSizingAboveThreshold(systemId: Long, threshold: Int, limit: Long, offset: Long): List<MethodSizing> {
        return jdbi.withHandle<List<MethodSizing>, Exception> {
            val sql = "select m1.systemId, m1.moduleName, m1.packageName, m1.typeName, m1.methodName, m1.`lines` from MethodStatistic m1 " +
                    "where m1.createAt = (SELECT MAX(m2.createAt) FROM MethodStatistic m2 WHERE m2.systemId = :systemId) " +
                    "and m1.systemId = :systemId and m1.`lines`>:threshold order by m1.`lines` desc limit :limit offset :offset"
            it.createQuery(sql)
                    .bind("systemId", systemId)
                    .bind("threshold", threshold)
                    .bind("limit", limit)
                    .bind("offset", offset)
                    .mapTo(MethodSizing::class.java).list()
        }
    }

    override fun getClassSizingAboveThreshold(systemId: Long, threshold: Int, limit: Long, offset: Long): List<ClassSizing> {
        return jdbi.withHandle<List<ClassSizing>, Exception> {
            val sql = "select c1.systemId, c1.moduleName, c1.packageName, c1.typeName, c1.`lines` from ClassStatistic c1 " +
                    "where c1.createAt = (SELECT MAX(c2.createAt) FROM ClassStatistic c2 WHERE c2.systemId = :systemId) " +
                    "and c1.systemId = :systemId and c1.`lines`>:threshold order by c1.`lines` desc limit :limit offset :offset"
            it.createQuery(sql)
                    .bind("systemId", systemId)
                    .bind("threshold", threshold)
                    .bind("limit", limit)
                    .bind("offset", offset)
                    .mapTo(ClassSizing::class.java).list()
        }
    }

    override fun getMethodSizingAboveThresholdCount(systemId: Long, threshold: Int): Long {
        return jdbi.withHandle<Long, Exception> {
            val sql = "select count(1) from MethodStatistic m1 where m1.createAt = (SELECT MAX(m2.createAt) FROM MethodStatistic m2 WHERE m2.systemId = :systemId) and m1.systemId = :systemId and m1.`lines`>:threshold"
            it.createQuery(sql)
                    .bind("systemId", systemId)
                    .bind("threshold", threshold)
                    .mapTo(Long::class.java)
                    .one()
        }
    }

    override fun getClassSizingAboveThresholdCount(systemId: Long, threshold: Int): Long {
        return jdbi.withHandle<Long, Exception> {
            val sql = "select count(1) from ClassStatistic c1 where c1.createAt = (SELECT MAX(c2.createAt) FROM ClassStatistic c2 WHERE c2.systemId = :systemId) and c1.systemId = :systemId and c1.`lines`>:threshold"
            it.createQuery(sql)
                    .bind("systemId", systemId)
                    .bind("threshold", threshold)
                    .mapTo(Long::class.java)
                    .one()
        }
    }
}
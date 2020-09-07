package com.thoughtworks.archguard.report.infrastructure

import com.thoughtworks.archguard.report.domain.sizing.ClassSizingWithLine
import com.thoughtworks.archguard.report.domain.sizing.ClassSizingWithMethodCount
import com.thoughtworks.archguard.report.domain.sizing.MethodSizing
import com.thoughtworks.archguard.report.domain.model.PackageSizing
import com.thoughtworks.archguard.report.domain.sizing.SizingRepository
import org.jdbi.v3.core.Jdbi
import org.springframework.stereotype.Repository


@Repository
class SizingRepositoryImpl(val jdbi: Jdbi) : SizingRepository {
    override fun getPackageSizingAboveLineThresholdCount(systemId: Long, threshold: Int): Long {
        return jdbi.withHandle<Long, Exception> {
            val sql = """
                select count(1)
                    from (select count(1)
                          from ClassStatistic c1
                          where c1.createAt = (SELECT MAX(c2.createAt) FROM ClassStatistic c2 WHERE c2.systemId = :systemId)
                            and c1.`lines` > :threshold
                          group by c1.packageName, c1.moduleName) as linesCount
            """.trimIndent()
            it.createQuery(sql)
                    .bind("systemId", systemId)
                    .bind("threshold", threshold)
                    .mapTo(Long::class.java)
                    .one()
        }
    }

    override fun getPackageSizingAboveLineThreshold(systemId: Long, threshold: Int, limit: Long, offset: Long): List<PackageSizing> {
        return jdbi.withHandle<List<PackageSizing>, Exception> {
            val sql = """
                select c1.systemId, c1.packageName, c1.moduleName, sum(c1.`lines`) `lines`
                from ClassStatistic c1
                where c1.createAt = (SELECT MAX(c2.createAt) FROM ClassStatistic c2 WHERE c2.systemId = :systemId)
                and c1.`lines` > :threshold
                group by c1.systemId, c1.packageName, c1.moduleName
                order by `lines` desc
                limit :limit offset :offset
            """.trimIndent()
            it.createQuery(sql)
                    .bind("systemId", systemId)
                    .bind("threshold", threshold)
                    .bind("limit", limit)
                    .bind("offset", offset)
                    .mapTo(PackageSizing::class.java).list()
        }
    }

    override fun getMethodSizingAboveLineThreshold(systemId: Long, threshold: Int, limit: Long, offset: Long): List<MethodSizing> {
        return jdbi.withHandle<List<MethodSizing>, Exception> {
            val sql = "select m1.id, m1.systemId, m1.moduleName, m1.packageName, m1.typeName, m1.methodName, m1.`lines` from MethodStatistic m1 " +
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

    override fun getClassSizingAboveLineThreshold(systemId: Long, threshold: Int, limit: Long, offset: Long): List<ClassSizingWithLine> {
        return jdbi.withHandle<List<ClassSizingWithLine>, Exception> {
            val sql = "select c1.id, c1.systemId, c1.moduleName, c1.packageName, c1.typeName, c1.`lines` from ClassStatistic c1 " +
                    "where c1.createAt = (SELECT MAX(c2.createAt) FROM ClassStatistic c2 WHERE c2.systemId = :systemId) " +
                    "and c1.systemId = :systemId and c1.`lines`>:threshold order by c1.`lines` desc limit :limit offset :offset"
            it.createQuery(sql)
                    .bind("systemId", systemId)
                    .bind("threshold", threshold)
                    .bind("limit", limit)
                    .bind("offset", offset)
                    .mapTo(ClassSizingWithLine::class.java).list()
        }
    }

    override fun getClassSizingListAboveMethodCountThreshold(systemId: Long, threshold: Int, limit: Long, offset: Long): List<ClassSizingWithMethodCount> {
        return jdbi.withHandle<List<ClassSizingWithMethodCount>, Exception> {
            val sql = "select m1.id, m1.systemId, m1.moduleName, m1.packageName, m1.typeName, count(1) as methodCount " +
                    "from MethodStatistic m1 " +
                    "where m1.createAt = (SELECT MAX(c2.createAt) FROM MethodStatistic c2 WHERE c2.systemId = :systemId) " +
                    "and m1.systemId = :systemId " +
                    "group by m1.typeName, m1.packageName, m1.moduleName having methodCount > :threshold " +
                    "order by methodCount desc limit :limit offset :offset"
            it.createQuery(sql)
                    .bind("systemId", systemId)
                    .bind("threshold", threshold)
                    .bind("limit", limit)
                    .bind("offset", offset)
                    .mapTo(ClassSizingWithMethodCount::class.java).list()
        }
    }

    override fun getMethodSizingAboveLineThresholdCount(systemId: Long, threshold: Int): Long {
        return jdbi.withHandle<Long, Exception> {
            val sql = "select count(1) from MethodStatistic m1 where m1.createAt = (SELECT MAX(m2.createAt) FROM MethodStatistic m2 WHERE m2.systemId = :systemId) and m1.systemId = :systemId and m1.`lines`>:threshold"
            it.createQuery(sql)
                    .bind("systemId", systemId)
                    .bind("threshold", threshold)
                    .mapTo(Long::class.java)
                    .one()
        }
    }

    override fun getClassSizingAboveLineThresholdCount(systemId: Long, threshold: Int): Long {
        return jdbi.withHandle<Long, Exception> {
            val sql = "select count(1) from ClassStatistic c1 where c1.createAt = (SELECT MAX(c2.createAt) FROM ClassStatistic c2 WHERE c2.systemId = :systemId) and c1.systemId = :systemId and c1.`lines`>:threshold"
            it.createQuery(sql)
                    .bind("systemId", systemId)
                    .bind("threshold", threshold)
                    .mapTo(Long::class.java)
                    .one()
        }
    }

    override fun getClassSizingListAboveMethodCountThresholdCount(systemId: Long, threshold: Int): Long {
        return jdbi.withHandle<Long, Exception> {
            val sql = "select count(1) from " +
                    "(select count(1) as methodCount " +
                    "from MethodStatistic m1 " +
                    "where m1.createAt = (SELECT MAX(m2.createAt) FROM MethodStatistic m2 WHERE m2.systemId = :systemId) " +
                    "and m1.systemId = :systemId " +
                    "group by m1.typeName, m1.packageName, m1.moduleName " +
                    "having methodCount > :threshold) as groupCount"
            it.createQuery(sql)
                    .bind("systemId", systemId)
                    .bind("threshold", threshold)
                    .mapTo(Long::class.java)
                    .one()
        }
    }

}

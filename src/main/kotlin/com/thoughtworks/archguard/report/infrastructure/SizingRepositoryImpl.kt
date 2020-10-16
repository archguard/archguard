package com.thoughtworks.archguard.report.infrastructure

import com.thoughtworks.archguard.report.domain.overview.calculator.BadSmellCalculateResult
import com.thoughtworks.archguard.report.domain.sizing.*
import org.jdbi.v3.core.Jdbi
import org.springframework.stereotype.Repository


@Repository
class SizingRepositoryImpl(val jdbi: Jdbi) : SizingRepository {
    override fun getModuleSizingListAbovePackageCountThresholdCount(systemId: Long, threshold: Int): Long {
        return jdbi.withHandle<Long, Exception> {
            val sql = """
                select count(1)
                    from (
                             select count(distinct packageName) packageCount
                             from ClassStatistic c1
                             where c1.createAt = (SELECT MAX(c2.createAt) FROM ClassStatistic c2 WHERE c2.systemId = :systemId)
                             group by c1.systemId, c1.moduleName
                         ) as c
                    where c.packageCount > :threshold
            """.trimIndent()
            it.createQuery(sql)
                    .bind("systemId", systemId)
                    .bind("threshold", threshold)
                    .mapTo(Long::class.java)
                    .findOne()
                    .orElse(0)
        }
    }

    override fun getModuleSizingListAbovePackageCountBadSmellResult(systemId: Long, thresholdRanges: Array<LongRange>): BadSmellCalculateResult {
        return jdbi.withHandle<BadSmellCalculateResult, Exception> {
            val sql = """
                select sum(CASE when c.packageCount >= :level1Start and c.packageCount < :level1End then 1 else 0 end) AS 'level1',
                       sum(CASE when c.packageCount >= :level2Start and c.packageCount < :level2End then 1 else 0 end) AS 'level2',
                       sum(CASE when c.packageCount >= :level3Start then 1 else 0 end)                                  AS 'level3'
                from (
                         select count(distinct packageName) packageCount
                         from ClassStatistic c1
                         where c1.createAt = (SELECT MAX(c2.createAt) FROM ClassStatistic c2 WHERE c2.systemId = :systemId)
                         group by c1.systemId, c1.moduleName
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

    override fun getModuleSizingListAbovePackageCountThreshold(systemId: Long, threshold: Int, limit: Long, offset: Long): List<ModuleSizing> {
        return jdbi.withHandle<List<ModuleSizing>, Exception> {
            val sql = """
                select systemId, moduleName, packageCount, classCount, `lines`
                    from (
                             select c1.systemId, c1.moduleName, count(distinct packageName) packageCount, count(1) classCount, sum(c1.`lines`) `lines`
                             from ClassStatistic c1
                             where c1.createAt = (SELECT MAX(c2.createAt) FROM ClassStatistic c2 WHERE c2.systemId = :systemId)
                             group by c1.systemId, c1.moduleName
                             order by `packageCount` desc
                             limit :limit offset :offset
                         ) as c
                    where c.packageCount > :threshold;
            """.trimIndent()
            it.createQuery(sql)
                    .bind("systemId", systemId)
                    .bind("threshold", threshold)
                    .bind("limit", limit)
                    .bind("offset", offset)
                    .mapTo(ModuleSizing::class.java).list()
        }
    }

    override fun getModuleSizingAboveLineThresholdCount(systemId: Long, threshold: Int): Long {
        return jdbi.withHandle<Long, Exception> {
            val sql = """
                select count(1)
                    from (
                             select  sum(c1.`lines`) `lines`
                             from ClassStatistic c1
                             where c1.createAt = (SELECT MAX(c2.createAt) FROM ClassStatistic c2 WHERE c2.systemId = :systemId)
                             group by c1.systemId, c1.moduleName
                         ) as c
                    where c.lines > :threshold;
            """.trimIndent()
            it.createQuery(sql)
                    .bind("systemId", systemId)
                    .bind("threshold", threshold)
                    .mapTo(Long::class.java)
                    .findOne()
                    .orElse(0)
        }
    }

    override fun getModuleSizingAboveLineBadSmellResult(systemId: Long, thresholdRanges: Array<LongRange>): BadSmellCalculateResult {
        return jdbi.withHandle<BadSmellCalculateResult, Exception> {
            val sql = """
                select sum(CASE when c.lineCount >= :level1Start and c.lineCount < :level1End then 1 else 0 end) AS 'level1',
                       sum(CASE when c.lineCount >= :level2Start and c.lineCount < :level2End then 1 else 0 end) AS 'level2',
                       sum(CASE when c.lineCount >= :level3Start then 1 else 0 end)                               AS 'level3'
                from (
                         select sum(c1.`lines`) lineCount
                         from ClassStatistic c1
                         where c1.createAt = (SELECT MAX(c2.createAt) FROM ClassStatistic c2 WHERE c2.systemId = :systemId)
                         group by c1.systemId, c1.moduleName
                     ) as c;
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

    override fun getModuleSizingAboveLineThreshold(systemId: Long, threshold: Int, limit: Long, offset: Long): List<ModuleSizing> {
        return jdbi.withHandle<List<ModuleSizing>, Exception> {
            val sql = """
                select systemId, moduleName, packageCount, classCount, `lines`
                    from (
                             select c1.systemId, c1.moduleName, count(distinct packageName) packageCount, count(1) classCount, sum(c1.`lines`) `lines`
                             from ClassStatistic c1
                             where c1.createAt = (SELECT MAX(c2.createAt) FROM ClassStatistic c2 WHERE c2.systemId = :systemId)
                             group by c1.systemId, c1.moduleName
                             order by `lines` desc
                             limit :limit offset :offset
                         ) as c
                    where c.lines > :threshold;
            """.trimIndent()
            it.createQuery(sql)
                    .bind("systemId", systemId)
                    .bind("threshold", threshold)
                    .bind("limit", limit)
                    .bind("offset", offset)
                    .mapTo(ModuleSizing::class.java).list()
        }
    }

    override fun getPackageSizingListAboveClassCountThresholdCount(systemId: Long, threshold: Int): Long {
        return jdbi.withHandle<Long, Exception> {
            val sql = """
               select count(1) from 
                (select count(name) as count from JClass
                    where system_id = :systemId and is_test=false and loc is not NULL group by module, package_name
                    having count > :threshold) as p
            """.trimIndent()
            it.createQuery(sql)
                    .bind("systemId", systemId)
                    .bind("threshold", threshold)
                    .mapTo(Long::class.java)
                    .findOne()
                    .orElse(0)
        }
    }

    override fun getPackageSizingListAboveClassCountBadSmellResult(systemId: Long, thresholdRanges: Array<LongRange>): BadSmellCalculateResult {
        return jdbi.withHandle<BadSmellCalculateResult, Exception> {
            val sql = """
                select sum(CASE when c.classCount >= :level1Start and c.classCount < :level1End then 1 else 0 end) AS 'level1',
                       sum(CASE when c.classCount >= :level2Start and c.classCount < :level2End then 1 else 0 end) AS 'level2',
                       sum(CASE when c.classCount >= :level3Start then 1 else 0 end)                                AS 'level3'
                from (
                         select count(1) classCount
                         from ClassStatistic c1
                         where c1.createAt = (SELECT MAX(c2.createAt) FROM ClassStatistic c2 WHERE c2.systemId = :systemId)
                         group by c1.systemId, c1.moduleName, c1.packageName
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

    override fun getPackageSizingListAboveClassCountThreshold(systemId: Long, threshold: Int, limit: Long, offset: Long): List<PackageSizing> {
        return jdbi.withHandle<List<PackageSizing>, Exception> {
            val sql = """
                select uuid() as id, count(name) as classCount,sum(loc) as `lines` module as moduleName, system_id, package_name from JClass
                    where system_id =:systemId and is_test=false and loc is not NULL group by module, package_name
                    having count > :threshold order by count desc
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

    override fun getPackageSizingAboveLineThresholdCount(systemId: Long, threshold: Int): Long {
        return jdbi.withHandle<Long, Exception> {
            val sql = """
              select count(1) from (
                select sum(loc) as `lines` from JClass 
                  where system_id = :systemId and is_test=false and loc is not NULL group by module, package_name 
                  having `lines` > :threshold) as p 
            """.trimIndent()
            it.createQuery(sql)
                    .bind("systemId", systemId)
                    .bind("threshold", threshold)
                    .mapTo(Long::class.java)
                    .findOne()
                    .orElse(0)
        }
    }

    override fun getPackageSizingAboveLineBadSmellResult(systemId: Long, thresholdRanges: Array<LongRange>): BadSmellCalculateResult {
        return jdbi.withHandle<BadSmellCalculateResult, Exception> {
            val sql = """
                select sum(CASE when c.lineCount >= :level1Start and c.lineCount < :level1End then 1 else 0 end) AS 'level1',
                       sum(CASE when c.lineCount >= :level2Start and c.lineCount < :level2End then 1 else 0 end) AS 'level2',
                       sum(CASE when c.lineCount >= :level3Start then 1 else 0 end)                               AS 'level3'
                from (
                         select sum(c1.`lines`) lineCount
                         from ClassStatistic c1
                         where c1.createAt = (SELECT MAX(c2.createAt) FROM ClassStatistic c2 WHERE c2.systemId = :systemId)
                         group by c1.systemId, c1.moduleName, c1.packageName
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

    override fun getPackageSizingAboveLineThreshold(systemId: Long, threshold: Int, limit: Long, offset: Long): List<PackageSizing> {
        return jdbi.withHandle<List<PackageSizing>, Exception> {
            val sql = """
                select sum(loc) as `lines`, count(name) as classCount, module, system_id, package_name from JClass 
                  where system_id = :systemId and is_test=false and loc is not NULL group by module, package_name 
                  having `lines` > :threshold order by `lines` desc
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
            val sql = "select id, system_id,  module, class_name, package_name, name, loc from JMethod " +
                    "where system_id = :systemId and loc>:threshold and is_test=false order by loc desc limit :limit offset :offset"
            it.createQuery(sql)
                    .bind("systemId", systemId)
                    .bind("threshold", threshold)
                    .bind("limit", limit)
                    .bind("offset", offset)
                    .mapTo(JMethodPO::class.java).list()
                    .map { po -> po.toMethodSizing() }
        }
    }

    override fun getClassSizingAboveLineThreshold(systemId: Long, threshold: Int, limit: Long, offset: Long): List<ClassSizingWithLine> {
        return jdbi.withHandle<List<ClassSizingWithLine>, Exception> {
            val sql = "select id, system_id, module, class_name, package_name, loc from JClass " +
                    "where system_id = :systemId and loc>:threshold and is_test=false order by loc desc limit :limit offset :offset"
            it.createQuery(sql)
                    .bind("systemId", systemId)
                    .bind("threshold", threshold)
                    .bind("limit", limit)
                    .bind("offset", offset)
                    .mapTo(JClassPO::class.java).list()
                    .map { po -> po.toClassSizingWithLine() }
        }
    }

    override fun getClassSizingListAboveMethodCountThreshold(systemId: Long, threshold: Int, limit: Long, offset: Long): List<ClassSizingWithMethodCount> {
        return jdbi.withHandle<List<ClassSizingWithMethodCount>, Exception> {
            val sql = "select uuid() as id, count(name) as count, module,system_id, class_name, package_name from JMethod " +
                    "where system_id = :systemId and is_test=false and loc is not NULL " +
                    "group by module, class_name, package_name " +
                    "having count>:threshold order by count desc " +
                    "limit :limit offset :offset"
            it.createQuery(sql)
                    .bind("systemId", systemId)
                    .bind("threshold", threshold)
                    .bind("limit", limit)
                    .bind("offset", offset)
                    .mapTo(JClassPO::class.java).list()
                    .map { po -> po.toClassSizingWithMethodCount() }
        }
    }

    override fun getClassSizingListAboveMethodCountThresholdCount(systemId: Long, threshold: Int): Long {
        return jdbi.withHandle<Long, Exception> {
            val table = "select count(name) as count from JMethod where system_id = :systemId and is_test=false and loc is not NULL  " +
                    "group by clzname " +
                    "having count>:threshold "
            val sql = "select count(1) from ($table) as c"
            it.createQuery(sql)
                    .bind("systemId", systemId)
                    .bind("threshold", threshold)
                    .mapTo(Long::class.java)
                    .findOne()
                    .orElse(0)
        }
    }

    override fun getMethodSizingAboveLineThresholdCount(systemId: Long, threshold: Int): Long {
        return jdbi.withHandle<Long, Exception> {
            val sql = "select count(1) from JMethod where system_id = :systemId and loc>:threshold and is_test=false"
            it.createQuery(sql)
                    .bind("systemId", systemId)
                    .bind("threshold", threshold)
                    .mapTo(Long::class.java)
                    .findOne()
                    .orElse(0)
        }
    }

    override fun getMethodSizingAboveLineBadSmellResult(systemId: Long, thresholdRanges: Array<LongRange>): BadSmellCalculateResult {
        return jdbi.withHandle<BadSmellCalculateResult, Exception> {
            val sql = """
                select sum(CASE when c.methodCount >= :level1Start and c.methodCount < :level1End then 1 else 0 end) AS 'level1',
                       sum(CASE when c.methodCount >= :level2Start and c.methodCount < :level2End then 1 else 0 end) AS 'level2',
                       sum(CASE when c.methodCount >= :level3Start then 1 else 0 end)                                 AS 'level3'
                from (
                         select m1.`lines` methodCount
                         from MethodStatistic m1
                         where m1.createAt = (SELECT MAX(m2.createAt) FROM MethodStatistic m2 WHERE m2.systemId = :systemId)
                           and m1.systemId = :systemId
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

    override fun getClassSizingAboveLineThresholdCount(systemId: Long, threshold: Int): Long {
        return jdbi.withHandle<Long, Exception> {
            val sql = "select count(1) from JClass where system_id = :systemId and loc>:threshold and is_test=false"
            it.createQuery(sql)
                    .bind("systemId", systemId)
                    .bind("threshold", threshold)
                    .mapTo(Long::class.java)
                    .findOne()
                    .orElse(0)
        }
    }

    override fun getClassSizingAboveLineBadSmellResult(systemId: Long, thresholdRanges: Array<LongRange>): BadSmellCalculateResult {
        return jdbi.withHandle<BadSmellCalculateResult, Exception> {
            val sql = """
                select sum(CASE when c.lineCount >= :level1Start and c.lineCount < :level1End then 1 else 0 end) AS 'level1',
                       sum(CASE when c.lineCount >= :level2Start and c.lineCount < :level2End then 1 else 0 end) AS 'level2',
                       sum(CASE when c.lineCount >= :level3Start then 1 else 0 end)                               AS 'level3'
                from (
                         select `lines` as lineCount
                         from ClassStatistic c1
                         where c1.createAt = (SELECT MAX(c2.createAt) FROM ClassStatistic c2 WHERE c2.systemId = :systemId)
                           and c1.systemId = :systemId
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

    override fun getClassSizingListAboveMethodCountBadSmellResult(systemId: Long, thresholdRanges: Array<LongRange>): BadSmellCalculateResult {
        return jdbi.withHandle<BadSmellCalculateResult, Exception> {
            val sql = """
                select sum(CASE when c.methodCount >= :level1Start and c.methodCount < :level1End then 1 else 0 end) AS 'level1',
                       sum(CASE when c.methodCount >= :level2Start and c.methodCount < :level2End then 1 else 0 end) AS 'level2',
                       sum(CASE when c.methodCount >= :level3Start then 1 else 0 end)                                AS 'level3'
                from (
                         select count(1) as methodCount
                         from MethodStatistic m1
                         where m1.createAt = (SELECT MAX(m2.createAt) FROM MethodStatistic m2 WHERE m2.systemId = :systemId)
                           and m1.systemId = :systemId
                         group by m1.typeName, m1.packageName, m1.moduleName
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
}

package com.thoughtworks.archguard.report.infrastructure

import com.thoughtworks.archguard.report.domain.sizing.ClassSizingWithLine
import com.thoughtworks.archguard.report.domain.sizing.ClassSizingWithMethodCount
import com.thoughtworks.archguard.report.domain.sizing.MethodSizing
import com.thoughtworks.archguard.report.domain.sizing.ModuleSizing
import com.thoughtworks.archguard.report.domain.sizing.PackageSizing
import com.thoughtworks.archguard.report.domain.sizing.SizingRepository
import org.jdbi.v3.core.Jdbi
import org.springframework.stereotype.Repository

@Repository
class SizingRepositoryImpl(val jdbi: Jdbi) : SizingRepository {
    override fun getModuleSizingListAbovePackageCountThresholdCount(systemId: Long, threshold: Int): Long {
        return jdbi.withHandle<Long, Exception> {
            val sql = """
                select count(1)
                    from (
                             select count(distinct package_name) as packageCount from code_class 
                             where system_id = :systemId and is_test=false and loc is not NULL group by module
                             having packageCount > :threshold
                         ) as m

            """.trimIndent()
            it.createQuery(sql)
                .bind("systemId", systemId)
                .bind("threshold", threshold)
                .mapTo(Long::class.java)
                .findOne()
                .orElse(0)
        }
    }

    override fun getModuleSizingListAbovePackageCountThreshold(systemId: Long, threshold: Int, limit: Long, offset: Long): List<ModuleSizing> {
        return jdbi.withHandle<List<ModuleSizing>, Exception> {
            val sql = """
                 select uuid() as id, count(class_name) as classCount, sum(loc) as `lines`,
                   count(distinct package_name) as packageCount, module as moduleName, system_id from code_class
                   where system_id = :systemId and is_test=false and loc is not NULL group by module
                   having packageCount > :threshold order by packageCount desc 
                   limit :limit offset :offset
            """.trimIndent()
            it.createQuery(sql)
                .bind("systemId", systemId)
                .bind("threshold", threshold)
                .bind("limit", limit)
                .bind("offset", offset)
                .mapTo(ModuleSizing::class.java).list()
        }
    }

    override fun getModuleSizingListAbovePackageCountThresholdCount(systemId: Long, threshold: Int, module: String): Long {
        return jdbi.withHandle<Long, Exception> {
            val sql = """
                select count(1)
                    from (
                             select count(distinct package_name) as packageCount from code_class  
                             where system_id = :systemId  and `module` like '%$module%'   
                             and is_test=false and loc is not NULL group by module 
                             having packageCount > :threshold 
                         ) as m

            """.trimIndent()
            it.createQuery(sql)
                .bind("systemId", systemId)
                .bind("threshold", threshold)
                .mapTo(Long::class.java)
                .findOne()
                .orElse(0)
        }
    }

    override fun getModuleSizingListAbovePackageCountThresholdByFilterSizing(systemId: Long, threshold: Int, limit: Long, offset: Long, module: String): List<ModuleSizing> {
        return jdbi.withHandle<List<ModuleSizing>, Exception> {

            val sql = """
                 select uuid() as id, count(class_name) as classCount, sum(loc) as `lines`,
                   count(distinct package_name) as packageCount, module as moduleName, system_id from code_class
                   where system_id = :systemId and is_test=false and `module` like '%$module%' 
                    and loc is not NULL group by module
                   having packageCount > :threshold order by packageCount desc 
                   limit :limit offset :offset
            """.trimIndent()

            println("sql :   $sql")
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
                             select sum(loc) as `lines` from code_class
                             where system_id = :systemId and is_test=false and loc is not NULL group by module
                             having `lines` > :threshold 
                         ) as m
            """.trimIndent()
            it.createQuery(sql)
                .bind("systemId", systemId)
                .bind("threshold", threshold)
                .mapTo(Long::class.java)
                .findOne()
                .orElse(0)
        }
    }

    override fun getModuleSizingAboveLineThreshold(systemId: Long, threshold: Int, limit: Long, offset: Long): List<ModuleSizing> {
        return jdbi.withHandle<List<ModuleSizing>, Exception> {
            val sql = """
                select uuid() as id, count(class_name) as classCount, sum(loc) as `lines`,
                  count(distinct package_name) as packageCount, module as moduleName, system_id from code_class
                  where system_id = :systemId and is_test=false and loc is not NULL group by module
                  having `lines` > :threshold order by `lines` desc 
                  limit :limit offset :offset
            """.trimIndent()
            it.createQuery(sql)
                .bind("systemId", systemId)
                .bind("threshold", threshold)
                .bind("limit", limit)
                .bind("offset", offset)
                .mapTo(ModuleSizing::class.java).list()
        }
    }

    override fun getModuleSizingAboveLineThresholdCount(systemId: Long, threshold: Int, module: String): Long {
        return jdbi.withHandle<Long, Exception> {
            val sql = """
                select count(1)
                    from (
                             select sum(loc) as `lines` from code_class
                             where system_id = :systemId and is_test=false  
                             and `module` like '%$module%' 
                             and loc is not NULL group by module 
                             having `lines` > :threshold 
                         ) as m
            """.trimIndent()
            it.createQuery(sql)
                .bind("systemId", systemId)
                .bind("threshold", threshold)
                .mapTo(Long::class.java)
                .findOne()
                .orElse(0)
        }
    }

    override fun getModuleSizingAboveLineThresholdByFilterSizing(systemId: Long, threshold: Int, limit: Long, offset: Long, module: String): List<ModuleSizing> {
        return jdbi.withHandle<List<ModuleSizing>, Exception> {

            val sql = """
                select uuid() as id, count(class_name) as classCount, sum(loc) as `lines`,
                  count(distinct package_name) as packageCount, module as moduleName, system_id from code_class
                  where system_id = :systemId and `module` like '%$module%'   
                  and is_test=false and loc is not NULL group by module
                  having `lines` > :threshold order by `lines` desc 
                  limit :limit offset :offset
            """.trimIndent()
            println("sql :   $sql")
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
                (select count(name) as count from code_class
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

    override fun getPackageSizingListAboveClassCountThreshold(systemId: Long, threshold: Int, limit: Long, offset: Long): List<PackageSizing> {
        return jdbi.withHandle<List<PackageSizing>, Exception> {
            val sql = """
                select uuid() as id, count(name) as classCount,sum(loc) as `lines`, module as moduleName, system_id, package_name from code_class
                    where system_id =:systemId and is_test=false and loc is not NULL group by module, package_name
                    having classCount > :threshold order by classCount desc
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

    override fun getPackageSizingListAboveClassCountThresholdCount(systemId: Long, threshold: Int, filter: FilterSizingPO): Long {
        return jdbi.withHandle<Long, Exception> {
            val sql = """
               select count(1) from 
                (select count(name) as count from code_class 
                    where system_id = :systemId    
                    and ( `module` like '%${filter.module}%' and package_name like '%${filter.packageName}%' )
                    and is_test=false and loc is not NULL group by module, package_name 
                    having count > :threshold) as p
            """.trimIndent()
            println("sql :  $sql")
            it.createQuery(sql)
                .bind("systemId", systemId)
                .bind("threshold", threshold)
                .mapTo(Long::class.java)
                .findOne()
                .orElse(0)
        }
    }

    override fun getPackageSizingListAboveClassCountThresholdByFilterSizing(systemId: Long, threshold: Int, filter: FilterSizingPO): List<PackageSizing> {
        return jdbi.withHandle<List<PackageSizing>, Exception> {

            val sql = """
                select uuid() as id, count(name) as classCount,sum(loc) as `lines`, module as moduleName, system_id, package_name from code_class
                    where system_id =:systemId 
                    and ( `module` like '%${filter.module}%' and package_name like '%${filter.packageName}%' )
                    and is_test=false and loc is not NULL group by module, package_name
                    having classCount > :threshold order by classCount desc
                    limit :limit offset :offset
            """.trimIndent()
            println("sql :   $sql")
            it.createQuery(sql)
                .bind("systemId", systemId)
                .bind("threshold", threshold)
                .bind("limit", filter.limit)
                .bind("offset", filter.offset)
                .mapTo(PackageSizing::class.java).list()
        }
    }

    override fun getPackageSizingAboveLineThresholdCount(systemId: Long, threshold: Int): Long {
        return jdbi.withHandle<Long, Exception> {
            val sql = """
              select count(1) from (
                select sum(loc) as `lines` from code_class 
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

    override fun getPackageSizingAboveLineThreshold(systemId: Long, threshold: Int, limit: Long, offset: Long): List<PackageSizing> {
        return jdbi.withHandle<List<PackageSizing>, Exception> {
            val sql = """
                select sum(loc) as `lines`, count(name) as classCount, module as moduleName, system_id, package_name from code_class 
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

    override fun getPackageSizingAboveLineThresholdCount(systemId: Long, threshold: Int, filter: FilterSizingPO): Long {
        return jdbi.withHandle<Long, Exception> {
            val sql = """
              select count(1) from ( 
                select sum(loc) as `lines` from code_class 
                  where system_id = :systemId  
                  and ( `module` like '%${filter.module}%' and package_name like '%${filter.packageName}%' )               
                  and is_test=false and loc is not NULL group by module, package_name  
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

    override fun getPackageSizingAboveLineThresholdByFilterSizing(systemId: Long, threshold: Int, filter: FilterSizingPO): List<PackageSizing> {
        return jdbi.withHandle<List<PackageSizing>, Exception> {

            val sql = """
                select sum(loc) as `lines`, count(name) as classCount, module as moduleName, system_id, package_name from code_class 
                  where system_id = :systemId  
                  and ( `module` like '%${filter.module}%' and package_name like '%${filter.packageName}%' ) 
                  and is_test=false and loc is not NULL group by module, package_name  
                  having `lines` > :threshold order by `lines` desc 
                  limit :limit offset :offset 
            """.trimIndent()

            println("sql :   $sql")

            it.createQuery(sql)
                .bind("systemId", systemId)
                .bind("threshold", threshold)
                .bind("limit", filter.limit)
                .bind("offset", filter.offset)
                .mapTo(PackageSizing::class.java).list()
        }
    }

    override fun getMethodSizingAboveLineThreshold(systemId: Long, threshold: Int, limit: Long, offset: Long): List<MethodSizing> {
        return jdbi.withHandle<List<MethodSizing>, Exception> {
            val sql = "select id, system_id,  module, class_name, package_name, name, loc from code_method " +
                "where system_id = :systemId " +
                "and loc>:threshold " +
                "and is_test=false " +
                "order by loc desc " +
                "limit :limit offset :offset"
            it.createQuery(sql)
                .bind("systemId", systemId)
                .bind("threshold", threshold)
                .bind("limit", limit)
                .bind("offset", offset)
                .mapTo(JMethodPO::class.java).list()
                .map { po -> po.toMethodSizing() }
        }
    }

    override fun getMethodSizingAboveLineThresholdCount(systemId: Long, threshold: Int, filter: FilterSizingPO): Long {
        return jdbi.withHandle<Long, Exception> {
            val sql = "select count(1) " +
                "from code_method " +
                "where system_id = :systemId " +
                "and loc>:threshold " +
                "and is_test=false " +
                "and ( module like '%${filter.module}%' " +
                "and class_name like '%${filter.className}%' " +
                "and package_name like '%${filter.packageName}%' " +
                "and package_name != '' " +

                "and `name` like '%${filter.name}%' ) "

            println("sql :   $sql")

            it.createQuery(sql)
                .bind("systemId", systemId)
                .bind("threshold", threshold)
                .mapTo(Long::class.java)
                .findOne()
                .orElse(0)
        }
    }

    override fun getMethodSizingAboveLineThresholdByFilterSizing(systemId: Long, threshold: Int, filter: FilterSizingPO): List<MethodSizing> {
        return jdbi.withHandle<List<MethodSizing>, Exception> {
            val sql = "select id, system_id,  module, class_name, package_name, name, loc from code_method " +
                "where system_id = :systemId " +
                "and loc>:threshold " +
                "and ( module like '%${filter.module}%' " +
                "and class_name like '%${filter.className}%' " +
                "and package_name like '%${filter.packageName}%' " +
                "and package_name != '' " +

                "and `name` like '%${filter.name}%' ) " +
                "and is_test=false " +
                "order by loc desc " +
                "limit :limit offset :offset"

            it.createQuery(sql)
                .bind("systemId", systemId)
                .bind("threshold", threshold)
                .bind("limit", filter.limit)
                .bind("offset", filter.offset)
                .mapTo(JMethodPO::class.java).list()
                .map { po -> po.toMethodSizing() }
        }
    }

    override fun getClassSizingAboveLineThresholdByRequestSizing(systemId: Long, threshold: Int, filter: FilterSizingPO): List<ClassSizingWithLine> {
        return jdbi.withHandle<List<ClassSizingWithLine>, Exception> {
            val sql = "select id, system_id, module, class_name, package_name, loc " +
                "from code_class " +
                "where system_id = :systemId " +
                "and loc>:threshold " +

                "and ( module like '%${filter.module}%' " +
                "and class_name like '%${filter.className}%' " +
                "and package_name like '%${filter.packageName}%' ) " +
                "and package_name != '' " +

                "and is_test=false " +
                "order by loc desc " +
                "limit :limit offset :offset"

            it.createQuery(sql)
                .bind("systemId", systemId)
                .bind("threshold", threshold)
                .bind("limit", filter.limit)
                .bind("offset", filter.offset)
                .mapTo(JClassPO::class.java).list()
                .map { po -> po.toClassSizingWithLine() }
        }
    }

    override fun getClassSizingListAboveMethodCountThresholdByRequestSizing(systemId: Long, threshold: Int, filter: FilterSizingPO): List<ClassSizingWithMethodCount> {
        return jdbi.withHandle<List<ClassSizingWithMethodCount>, Exception> {

            var additionCondition =  ""

            if (filter.module.isNotEmpty() && filter.className.isNotEmpty() && filter.packageName.isNotEmpty()) {
                additionCondition = "and ( module like '%${filter.module}%' " +
                        "and class_name like '%${filter.className}%' " +
                        "and package_name like '%${filter.packageName}%' ) ";
            }

            val sql = "select uuid() as id, count(name) as count, module,system_id, class_name, package_name " +
                "from code_method " +
                "where system_id = :systemId " +
                "and is_test=false " +
                additionCondition +
                "and package_name != '' " +
                "and loc is not NULL " +
                "group by module, class_name, package_name " +
                "having count>:threshold order by count desc " +
                "limit :limit offset :offset"

            it.createQuery(sql)
                .bind("systemId", systemId)
                .bind("threshold", threshold)
                .bind("limit", filter.limit)
                .bind("offset", filter.offset)
                .mapTo(JClassPO::class.java).list()
                .map { po -> po.toClassSizingWithMethodCount() }
        }
    }

    override fun getClassSizingAboveLineThreshold(systemId: Long, threshold: Int, limit: Long, offset: Long): List<ClassSizingWithLine> {
        return jdbi.withHandle<List<ClassSizingWithLine>, Exception> {
            val sql = "select id, system_id, module, class_name, package_name, loc from code_class " +
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
            val sql = "select uuid() as id, count(name) as count, module,system_id, class_name, package_name from code_method " +
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
            val table = "select count(name) as count from code_method where system_id = :systemId and is_test=false and loc is not NULL " +
                "group by class_name " +
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
            val sql = "select count(1) from code_method where system_id = :systemId and loc>:threshold and is_test=false"
            it.createQuery(sql)
                .bind("systemId", systemId)
                .bind("threshold", threshold)
                .mapTo(Long::class.java)
                .findOne()
                .orElse(0)
        }
    }

    override fun getClassSizingAboveLineThresholdCount(systemId: Long, threshold: Int): Long {
        return jdbi.withHandle<Long, Exception> {
            val sql = "select count(1) from code_class where system_id = :systemId and loc>:threshold and is_test=false"
            it.createQuery(sql)
                .bind("systemId", systemId)
                .bind("threshold", threshold)
                .mapTo(Long::class.java)
                .findOne()
                .orElse(0)
        }
    }

    override fun getClassSizingAboveLineThresholdCount(systemId: Long, threshold: Int, filter: FilterSizingPO): Long {
        return jdbi.withHandle<Long, Exception> {
            val sql = "select count(1) " +
                "from code_class " +
                "where system_id = :systemId " +
                "and loc>:threshold " +
                "and is_test=false " +
                "and ( module like '%${filter.module}%' " +
                "and class_name like '%${filter.className}%' " +
                "and package_name like '%${filter.packageName}%' ) "

            it.createQuery(sql)
                .bind("systemId", systemId)
                .bind("threshold", threshold)
                .mapTo(Long::class.java)
                .findOne()
                .orElse(0)
        }
    }

    override fun getClassSizingListAboveMethodCountThresholdCount(systemId: Long, threshold: Int, filter: FilterSizingPO): Long {
        return jdbi.withHandle<Long, Exception> {
            val table = "select count(name) as count from code_method " +
                "where system_id = :systemId " +
                "and ( module like '%${filter.module}%' " +
                "and class_name like '%${filter.className}%' " +
                "and package_name like '%${filter.packageName}%' ) " +
                "and package_name != '' " +
                "and is_test=false " +
                "and loc is not NULL  " +
                "group by class_name " +
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
}

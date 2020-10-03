package com.thoughtworks.archguard.report.infrastructure

import com.thoughtworks.archguard.report.domain.testing.MethodInfo
import com.thoughtworks.archguard.report.domain.testing.TestBadSmellRepository
import org.jdbi.v3.core.Jdbi
import org.springframework.stereotype.Repository
import kotlin.streams.toList

@Repository
class TestBadSmellRepositoryImpl(val jdbi: Jdbi) : TestBadSmellRepository {

    /* static, not private, not internal method, not compiler auto generated methods */
    override fun getStaticMethodCount(systemId: Long): Long {
        return jdbi.withHandle<Long, Exception> {
            val sql = """
                select count(m.id) from method_access m inner join JMethod c where m.method_id = c.id  
                and m.system_id = :systemId and m.is_static=1 and m.is_private=0 
                and c.name not in ('<clinit>', 'main') and c.name not like '%$%'
            """.trimIndent()
            it.createQuery(sql)
                    .bind("systemId", systemId)
                    .mapTo(Long::class.java)
                    .one()
        }
    }

    override fun getStaticMethods(systemId: Long, limit: Long, offset: Long): List<MethodInfo> {
        return jdbi.withHandle<List<MethodInfo>, Exception> {
            val sql = "select c.id, c.system_id as systemId, c.module as moduleName, c.clzname as classFullName, " +
                    "c.name as methodName from method_access m " +
                    "inner join JMethod c where m.method_id = c.id and m.system_id=:systemId " +
                    "and m.is_static=1 and m.is_private=0 " +
                    "and c.name not in ('<clinit>', 'main') and c.name not like '%$%' " +
                    "limit :limit offset :offset"
            it.createQuery(sql)
                    .bind("systemId", systemId)
                    .bind("offset", offset)
                    .bind("limit", limit)
                    .mapTo(JMethodPO::class.java).list().map { it.toMethodInfo() }
        }
    }

    override fun getSleepTestMethodCount(systemId: Long): Long {
        return jdbi.withHandle<Long, Exception> {
            val sql = """
                select count(id) from JMethod where system_id=:systemId and is_test=1 
                and id in (select mc.a from _MethodCallees mc 
                where mc.b in (select m.id from JMethod m where name in ('sleep')))
            """.trimIndent()
            it.createQuery(sql)
                    .bind("systemId", systemId)
                    .mapTo(Long::class.java)
                    .one()
        }
    }

    override fun getSleepTestMethods(systemId: Long, limit: Long, offset: Long): List<MethodInfo> {
        return jdbi.withHandle<List<MethodInfo>, Exception> {
            val sql = "select id, system_id as systemId, module as moduleName, clzname as classFullName, " +
                    "name as methodName FROM JMethod where system_id=:systemId and is_test=1 " +
                    "and id in (select mc.a from _MethodCallees mc " +
                    "where mc.b in (select id from JMethod  where name in ('sleep'))) " +
                    "limit :limit offset :offset"
            it.createQuery(sql)
                    .bind("systemId", systemId)
                    .bind("offset", offset)
                    .bind("limit", limit)
                    .mapTo(JMethodPO::class.java).list().map { it.toMethodInfo() }
        }
    }

    override fun getEmptyTestMethodCount(systemId: Long): Long {
        return jdbi.withHandle<Long, Exception> {
            val sql = """
                SELECT count(JMethod.id) FROM JMethod LEFT JOIN _MethodCallees ON JMethod.id=_MethodCallees.a 
                WHERE _MethodCallees.a IS NULL and JMethod.system_id=:systemId and JMethod.is_test=1
            """.trimIndent()
            it.createQuery(sql)
                    .bind("systemId", systemId)
                    .mapTo(Long::class.java)
                    .one()
        }
    }

    override fun getEmptyTestMethods(systemId: Long, limit: Long, offset: Long): List<MethodInfo> {
        return jdbi.withHandle<List<MethodInfo>, Exception> {
            val sql = "SELECT m.id, m.system_id as systemId, m.module as moduleName, m.clzname as classFullName, " +
                    "m.name as methodName FROM JMethod m " +
                    "LEFT JOIN _MethodCallees mc ON m.id=mc.a " +
                    "WHERE mc.a IS NULL and m.system_id=:systemId and m.is_test=1 " +
                    "limit :limit offset :offset"
            it.createQuery(sql)
                    .bind("systemId", systemId)
                    .bind("offset", offset)
                    .bind("limit", limit)
                    .mapTo(JMethodPO::class.java).list().map { it.toMethodInfo() }
        }
    }

    override fun getIgnoreTestMethodCount(systemId: Long): Long {
        val ignoreAnnotations = listOf("org.junit.jupiter.api.Disabled", "org.junit.Ignore");
        return jdbi.withHandle<Long, Exception> {
            val sql = """
                select count(id) from JMethod 
                where id in (select targetId from JAnnotation 
                where system_id=:systemId and name in (<listOfAnnotation>))
            """.trimIndent()
            it.createQuery(sql)
                    .bind("systemId", systemId)
                    .bindList("listOfAnnotation", ignoreAnnotations)
                    .mapTo(Long::class.java)
                    .one()
        }
    }

    override fun getIgnoreTestMethods(systemId: Long, limit: Long, offset: Long): List<MethodInfo> {
        val ignoreAnnotations = listOf("org.junit.jupiter.api.Disabled", "org.junit.Ignore");
        return jdbi.withHandle<List<MethodInfo>, Exception> {
            val sql = "SELECT m.id, m.system_id as systemId, m.module as moduleName, m.clzname as classFullName, " +
                    "m.name as methodName FROM JMethod m " +
                    "where id in (select targetId from JAnnotation " +
                    "where system_id=:systemId and name in (<listOfAnnotation>)) " +
                    "limit :limit offset :offset"
            it.createQuery(sql)
                    .bind("systemId", systemId)
                    .bindList("listOfAnnotation", ignoreAnnotations)
                    .bind("offset", offset)
                    .bind("limit", limit)
                    .mapTo(JMethodPO::class.java).list().map { it.toMethodInfo() }
        }
    }

    override fun getUnassertTestMethodIds(systemId: Long): List<String> {
        val methodsWithoutAssert = getMethodsWithoutAssert(systemId)
        val junit4ExpectExceptionList = getMethodListWithJunit4ExpectException(systemId)
        return methodsWithoutAssert.stream().filter { m -> !junit4ExpectExceptionList.contains(m) }.toList()
    }

    private fun getMethodsWithoutAssert(systemId: Long): List<String> {
        return jdbi.withHandle<List<String>, Exception> {
            val sql = """
                    select m1.id from JMethod m1 join 
                    (select  mc.a as callee_id, GROUP_CONCAT(clzname SEPARATOR ', ') as callers from _MethodCallees mc 
                    join JMethod m on mc.b = m.id where mc.a in (select id from JMethod where system_id=:systemId and is_test=1) 
                    group by mc.a) as t1 on m1.id = t1.callee_id where callers not like '%Assert%'
                """.trimIndent()
            it.createQuery(sql)
                    .bind("systemId", systemId)
                    .mapTo(String::class.java)
                    .list()
        }
    }

    private fun getMethodListWithJunit4ExpectException(systemId: Long): List<String> {
        return jdbi.withHandle<List<String>, Exception> {
            val sql = """
                     select m.id from JMethod m join 
                    (select j.id, j.targetId, j.name, jv.value from JAnnotation j join JAnnotationValue jv on j.id = jv.annotationId 
                    where jv.system_id=:systemId and jv.key='expected') as t1 on m.id = t1.targetId where m.is_test=1
                """.trimIndent()
            it.createQuery(sql)
                    .bind("systemId", systemId)
                    .mapTo(String::class.java)
                    .list()
        }
    }


    override fun getMethodsByIds(ids: List<String>, limit: Long, offset: Long): List<MethodInfo> {
        return jdbi.withHandle<List<MethodInfo>, Exception> {
            val sql = "SELECT m.id, m.system_id as systemId, m.module as moduleName, m.clzname as classFullName, " +
                    "m.name as methodName FROM JMethod m where id in (<ids>) limit :limit offset :offset"
            it.createQuery(sql)
                    .bindList("ids", ids)
                    .bind("offset", offset)
                    .bind("limit", limit)
                    .mapTo(JMethodPO::class.java).list().map { it.toMethodInfo() }
        }
    }

    override fun getAssertMethodAboveThresholdIds(systemId: Long, multiAssertThreshold: Int): List<String> {
        return jdbi.withHandle<List<String>, Exception> {
            val sql = """
                    select m1.id from JMethod m1 join 
                    (select  mc.a as callee_id, count(*) as assert_count from _MethodCallees mc 
                    join JMethod m on mc.b = m.id where clzname like '%Assert%' and mc.a in 
                    (select id from JMethod where system_id=:systemId and is_test=1)  group by mc.a) as t1
                    on m1.id = t1.callee_id and t1.assert_count>:threshold order by t1.assert_count
                """.trimIndent()
            it.createQuery(sql)
                    .bind("systemId", systemId)
                    .bind("threshold", multiAssertThreshold)
                    .mapTo(String::class.java)
                    .list()
        }
    }

    override fun getRedundantPrintAboveThresholdIds(systemId: Long, redundantPrintThreshold: Int): List<String> {
        val clzNames = listOf("java.io.PrintStream");
        return jdbi.withHandle<List<String>, Exception> {
            val sql = """
                    select m1.id from JMethod m1 join 
                    (select  mc.a as callee_id, count(*) as print_count from _MethodCallees mc 
                    join JMethod m on mc.b = m.id where clzname in (<clzNames>) and mc.a in 
                    (select id from JMethod where system_id=:systemId and is_test=1)  group by mc.a) as t1
                    on m1.id = t1.callee_id and print_count>:threshold order by print_count
                """.trimIndent()
            it.createQuery(sql)
                    .bind("systemId", systemId)
                    .bindList("clzNames", clzNames)
                    .bind("threshold", redundantPrintThreshold)
                    .mapTo(String::class.java)
                    .list()
        }
    }
}
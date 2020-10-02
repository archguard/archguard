package com.thoughtworks.archguard.report.infrastructure

import com.thoughtworks.archguard.report.domain.testing.MethodInfo
import com.thoughtworks.archguard.report.domain.testing.TestBadSmellRepository
import org.jdbi.v3.core.Jdbi
import org.springframework.stereotype.Repository

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
}
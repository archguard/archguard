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
                select count(m.id) from method_access m inner join code_method c where m.method_id = c.id  
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
            val sql = "select c.id, c.system_id as systemId, c.module as module, c.class_name as className,  " +
                    "c.package_name as packageName, c.name as name from method_access m " +
                    "inner join code_method c where m.method_id = c.id and m.system_id=:systemId " +
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

    override fun countTestSmellByType(systemId: Long, type: String): Long {
        return jdbi.withHandle<Long, Exception> {
            val sql = """
                select count(id) from governance_issue where system_id = 1 and name = :type
            """.trimIndent()
            it.createQuery(sql)
                .bind("systemId", systemId)
                .bind("type", type)
                .mapTo(Long::class.java)
                .one()
        }
    }

    override fun getTestSmellByType(systemId: Long, type: String, limit: Long, offset: Long): List<TestSmellPO> {
        return jdbi.withHandle<List<TestSmellPO>, Exception> { it ->
            val sql = "select name, detail, full_name as fullName, position " +
                    "from governance_issue where system_id = 1 and name = :type " +
                    "limit :limit offset :offset"
            it.createQuery(sql)
                .bind("systemId", systemId)
                .bind("type", type)
                .bind("offset", offset)
                .bind("limit", limit)
                .mapTo(TestSmellPO::class.java).list().map { it.toTestSmellPO() }
        }
    }
}

package com.thoughtworks.archguard.report.infrastructure

import com.thoughtworks.archguard.report.domain.sizing.SystemOverviewRepository
import org.jdbi.v3.core.Jdbi
import org.springframework.stereotype.Repository

@Repository
class SystemOverviewRepositoryImpl(val jdbi: Jdbi) : SystemOverviewRepository {
    override fun getSystemInfoRepoBySystemId(systemId: Long): String {
        return jdbi.withHandle<String, Exception> {
            val sql = """
                select repo from system_info where id = :id
            """.trimIndent()
            it.createQuery(sql)
                    .bind("id", systemId)
                    .mapTo(String::class.java)
                    .one()
        }
    }

    override fun getSystemModuleCountBySystemId(systemId: Long): Long {
        return jdbi.withHandle<Long, Exception> {
            val sql = """
                select count(distinct moduleName) moduleCount
                    from ClassStatistic c1
                    where c1.createAt = (SELECT MAX(c2.createAt) FROM ClassStatistic c2 WHERE c2.systemId = :systemId)
            """.trimIndent()
            it.createQuery(sql)
                    .bind("systemId", systemId)
                    .mapTo(Long::class.java)
                    .one()
        }
    }

    override fun getSystemLineCountBySystemId(systemId: Long): Long {
        return jdbi.withHandle<Long, Exception> {
            val sql = """
                select sum(`lines`) lineCount
                    from ClassStatistic c1
                    where c1.createAt = (SELECT MAX(c2.createAt) FROM ClassStatistic c2 WHERE c2.systemId = :systemId)
                """.trimIndent()
            it.createQuery(sql)
                    .bind("systemId", systemId)
                    .mapTo(Long::class.java)
                    .one()
        }
    }

    override fun getContributorCountBySystemId(systemId: Long): Long {
        return jdbi.withHandle<Long, Exception> {
            val sql = """
                select count(distinct committer_name)
                    from commit_log 
                    WHERE system_id = :systemId
            """.trimIndent()
            it.createQuery(sql)
                    .bind("systemId", systemId)
                    .mapTo(Long::class.java)
                    .one()
        }
    }
}

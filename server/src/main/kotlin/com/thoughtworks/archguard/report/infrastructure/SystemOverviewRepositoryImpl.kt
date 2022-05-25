package com.thoughtworks.archguard.report.infrastructure

import com.thoughtworks.archguard.report.domain.overview.SystemLanguage
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
                select count(distinct module) from code_class where system_id=:systemId and module is not null
            """.trimIndent()
            it.createQuery(sql)
                .bind("systemId", systemId)
                .mapTo(Long::class.java)
                .one()
        }
    }

    override fun getLineCountBySystemIdWithLanguage(systemId: Long): List<SystemLanguage> {
        return jdbi.withHandle<List<SystemLanguage>, Exception> {
            val sql = """
                select language, COUNT(*) as fileCount, sum(line_count) as lineCount from scm_path_change_count where system_id=:systemId
                 group by language order by lineCount desc
            """.trimIndent()

            it.createQuery(sql)
                .bind("systemId", systemId)
                .mapTo(SystemLanguage::class.java)
                .list()
        }
    }
    override fun getSystemLineCountBySystemId(systemId: Long): Long {
        return jdbi.withHandle<Long, Exception> {
            val sql = """
                select sum(loc) from code_class where system_id=:systemId
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
                    from scm_commit_log 
                    WHERE system_id = :systemId
            """.trimIndent()
            it.createQuery(sql)
                .bind("systemId", systemId)
                .mapTo(Long::class.java)
                .one()
        }
    }
}

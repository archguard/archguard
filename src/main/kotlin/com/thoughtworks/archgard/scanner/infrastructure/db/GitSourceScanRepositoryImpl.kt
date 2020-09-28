package com.thoughtworks.archgard.scanner.infrastructure.db

import com.thoughtworks.archgard.scanner.domain.scanner.git.GitSourceScanRepo
import org.jdbi.v3.core.Jdbi
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import java.io.File

@Repository
class GitSourceScanRepositoryImpl(@Autowired val sqlScriptRunner: SqlScriptRunner) : GitSourceScanRepo {

    @Autowired
    lateinit var jdbi: Jdbi

    override fun cleanupCommitLog(systemId: Long) {
        jdbi.withHandle<Unit, Nothing> {
            it.createUpdate("delete from commit_log where system_id = :systemId")
                    .bindBean(systemId)
                    .execute()
        }
    }

    override fun cleanupChangeEntry(systemId: Long) {
        jdbi.withHandle<Unit, Nothing> {
            it.createUpdate("delete from change_entry where system_id = :systemId")
                    .bindBean(systemId)
                    .execute()
        }
    }

    override fun saveGitReport(sql: File) {
        sqlScriptRunner.run(sql)
    }
}
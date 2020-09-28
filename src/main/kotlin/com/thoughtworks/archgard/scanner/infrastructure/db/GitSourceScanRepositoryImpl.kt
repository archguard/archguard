package com.thoughtworks.archgard.scanner.infrastructure.db

import com.thoughtworks.archgard.scanner.domain.scanner.git.GitSourceScanRepo
import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.Jdbi
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import java.io.File

@Repository
class GitSourceScanRepositoryImpl(@Autowired val sqlScriptRunner: SqlScriptRunner) : GitSourceScanRepo {
    private val log = LoggerFactory.getLogger(GitSourceScanRepositoryImpl::class.java)

    @Autowired
    lateinit var jdbi: Jdbi

    override fun cleanupCommitLog(systemId: Long) {
        log.info("clean up commit_log for system {}", systemId)
        jdbi.withHandle<Any, RuntimeException> { handle: Handle ->
            handle.execute(
                    "delete from commit_log where system_id = ?", systemId)
        }
    }

    override fun cleanupChangeEntry(systemId: Long) {
        log.info("clean up change_entry for system {}", systemId)
        jdbi.withHandle<Any, RuntimeException> { handle: Handle ->
            handle.execute(
                    "delete from change_entry where system_id = ?", systemId)
        }
    }

    override fun saveGitReport(sql: File) {
        log.info("saving git report to DB")
        sqlScriptRunner.run(sql)
    }
}
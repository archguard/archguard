package com.thoughtworks.archgard.scanner.infrastructure.db

import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.Jdbi
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.jdbc.Sql

@SpringBootTest
@ActiveProfiles("test")
internal class GitSourceScanRepositoryImplTest {

    @Autowired
    lateinit var gitSourceScanRepositoryImpl: GitSourceScanRepositoryImpl

    @Autowired
    lateinit var jdbi: Jdbi

    @Test
    @Sql("classpath:sqls/insert_commit_log_and_change_log.sql")
    fun cleanupCommitLog() {
        checkCommitLogTableCount(2)
        gitSourceScanRepositoryImpl.cleanupCommitLog(0)
        checkCommitLogTableCount(0)
    }

    private fun checkCommitLogTableCount(count: Int) {
        val afterCount = jdbi.withHandle<Int, RuntimeException> { handle: Handle ->
            handle.createQuery("select count(*) from commit_log")
                    .mapTo(Int::class.java).one()
        }
        assertEquals(count, afterCount)
    }
}
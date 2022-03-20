package com.thoughtworks.archguard.scanner.infrastructure.db

import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.Jdbi
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.jdbc.Sql

@SpringBootTest
@ActiveProfiles("test")
@Disabled
internal class GitSourceScanRepositoryImplTest {

    @Autowired
    lateinit var gitSourceScanRepositoryImpl: GitSourceScanRepositoryImpl

    @Autowired
    lateinit var jdbi: Jdbi

    @Test
    @Sql("classpath:sqls/insert_commit_log_for_clean_up_test.sql")
    fun cleanupCommitLog() {
        checkCommitLogTableCount(2)
        gitSourceScanRepositoryImpl.cleanupCommitLog(1)
        checkCommitLogTableCount(0)
    }

    private fun checkCommitLogTableCount(count: Int) {
        val afterCount = jdbi.withHandle<Int, RuntimeException> { handle: Handle ->
            handle.createQuery("select count(*) from scm_commit_log where system_id=1")
                    .mapTo(Int::class.java).one()
        }
        assertEquals(count, afterCount)
    }
}
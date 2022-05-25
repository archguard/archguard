package com.thoughtworks.archguard.report.infrastructure

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.jdbc.Sql

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class SystemOverviewRepositoryImplTest {

    @Autowired
    lateinit var systemOverviewRepositoryImpl: SystemOverviewRepositoryImpl

    @Test
    @Sql("classpath:sqls/insert_commit_log_for_contributor_test.sql")
    fun getContributorCountBySystemId() {
        assertEquals(5, systemOverviewRepositoryImpl.getContributorCountBySystemId(6))
    }
}

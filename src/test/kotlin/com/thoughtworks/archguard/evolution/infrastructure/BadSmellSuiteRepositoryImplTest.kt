package com.thoughtworks.archguard.evolution.infrastructure

import com.thoughtworks.archguard.evolution.domain.BadSmellSuiteRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
internal class BadSmellSuiteRepositoryImplTest {

    @Autowired
    lateinit var repository: BadSmellSuiteRepository

    @Test
//    @Sql("classpath:sqls/insert_bad_smell_suite.sql")
    fun getAllBadSmellThresholdSuites() {
        val result = repository.getAllBadSmellThresholdSuites()
        assertEquals(2, result.size)
        assertEquals("架构评估一级指标", result[0].suiteName)
        assertEquals(2, result[0].thresholds.size)
    }
}
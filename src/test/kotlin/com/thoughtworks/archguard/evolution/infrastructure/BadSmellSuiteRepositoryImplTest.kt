package com.thoughtworks.archguard.evolution.infrastructure

import com.github.database.rider.core.api.dataset.DataSet
import com.github.database.rider.spring.api.DBRider
import com.thoughtworks.archguard.evolution.domain.BadSmellSuiteRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
@DBRider
internal class BadSmellSuiteRepositoryImplTest {

    @Autowired
    lateinit var repository: BadSmellSuiteRepository

    @Test
    @DataSet("expect/bad_smell_threshold_suite.yml")
    fun getAllBadSmellThresholdSuites() {
        val result = repository.getAllBadSmellThresholdSuites()
        assertEquals(1, result.size)
        assertEquals("架构评估一级指标", result[0].suiteName)
        assertEquals(2, result[0].thresholds.size)
    }
}
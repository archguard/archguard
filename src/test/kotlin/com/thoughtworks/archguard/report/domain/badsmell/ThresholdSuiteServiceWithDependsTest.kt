package com.thoughtworks.archguard.report.domain.badsmell

import com.github.database.rider.core.api.dataset.DataSet
import com.github.database.rider.spring.api.DBRider
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
@DBRider
internal class ThresholdSuiteServiceWithDependsTest {

    @Autowired
    lateinit var thresholdSuiteService: ThresholdSuiteServiceWithDepends

    @BeforeEach
    internal fun setUp() {
        thresholdSuiteService.initThresholdCache()
    }

    @Test
    @DataSet("expect/threshold_suite_get_value_test.yml")
    fun should_get_threshold_value() {
        val thresholdValue = thresholdSuiteService.getThresholdValue(1, ThresholdKey.SIZING_CLASS_BY_LOC)
        assertEquals(600, thresholdValue)
    }
}
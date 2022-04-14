package com.thoughtworks.archguard.evolution.domain

import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class BadSmellThresholdServiceTest {

    @MockK
    private lateinit var suiteRepository: BadSmellSuiteRepository

    @BeforeEach
    internal fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun should_get_all_suites() {
        val suites = listOf(
            BadSmellSuite(
                1, "Suite1", true,
                listOf(BadSmellGroup("sizing", listOf(BadSmellThreshold("name", "condition", 30))))
            )
        )
        every { suiteRepository.getSelectedBadSmellSuiteIdBySystem(1) }.returns(1)
        every { suiteRepository.getAllBadSmellThresholdSuites() }.returns(suites)
        val service = BadSmellThresholdService(suiteRepository)

        val result = service.getAllSuits()

        assertEquals(1, result.size)
    }

    @Test
    fun should_get_selected_suite_flag_by_system_id() {
        val suites = listOf(
            BadSmellSuite(
                1, "Suite1", true,
                listOf(BadSmellGroup("sizing", listOf(BadSmellThreshold("name", "condition", 30))))
            )
        )
        every { suiteRepository.getSelectedBadSmellSuiteIdBySystem(1) }.returns(1)
        every { suiteRepository.getAllBadSmellThresholdSuites() }.returns(suites)
        val service = BadSmellThresholdService(suiteRepository)

        val result = service.getBadSmellSuiteWithSelectedInfoBySystemId(1)

        assertEquals(1, result.size)
        assertTrue(result[0].isSelected)
    }
}

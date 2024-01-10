package com.thoughtworks.archguard.report.domain.overview.calculator;

import com.thoughtworks.archguard.report.domain.coupling.hub.ClassCouplingRepository
import org.archguard.smell.BadSmellLevel
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.springframework.stereotype.Component

@Component
class ClassHubCouplingCalculatorTest {

    private val classCouplingRepository = mock(ClassCouplingRepository::class.java)
    private val calculator = ClassHubCouplingCalculator(classCouplingRepository)

    @Test
    fun shouldReturnBadSmellLevelWhenCalculatingResult() {
        val systemId = 1L

        `when`(classCouplingRepository.getCouplingAboveBadSmellCalculateResult(systemId, calculator.getTypeCountLevelRanges()))
            .thenReturn(BadSmellLevel(level1 = 1, level2 = 2, level3 = 3))

        val result = calculator.getCalculateResult(systemId)

        assertEquals(BadSmellLevel(level1 = 1, level2 = 2, level3 = 3), result)
    }
}

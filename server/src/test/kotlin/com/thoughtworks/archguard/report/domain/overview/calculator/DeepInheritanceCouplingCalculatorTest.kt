package com.thoughtworks.archguard.report.domain.overview.calculator;

import com.thoughtworks.archguard.report.domain.coupling.deepinheritance.DeepInheritanceRepository
import org.archguard.smell.BadSmellLevel
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.springframework.stereotype.Component

@Component
class DeepInheritanceCouplingCalculatorTest {

    @Test
    fun `should return correct BadSmellLevel`() {
        // Given
        val systemId = 1L
        val deepInheritanceRepository = mock(DeepInheritanceRepository::class.java)
        val calculator = DeepInheritanceCouplingCalculator(deepInheritanceRepository)
        val expectedLevel = BadSmellLevel(0L, 0L, 1L)
        `when`(deepInheritanceRepository.getDitAboveBadSmellCalculateResult(systemId, calculator.getTypeCountLevelRanges()))
            .thenReturn(expectedLevel)

        // When
        val result = calculator.getCalculateResult(systemId)

        // Then
        assertEquals(expectedLevel, result)
    }
}

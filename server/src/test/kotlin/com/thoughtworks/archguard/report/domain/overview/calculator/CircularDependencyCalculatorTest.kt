package com.thoughtworks.archguard.report.domain.overview.calculator;

import com.thoughtworks.archguard.report.domain.coupling.circulardependency.CircularDependencyRepository
import com.thoughtworks.archguard.report.domain.coupling.circulardependency.CircularDependencyType
import org.archguard.smell.BadSmellLevel
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import kotlin.test.assertEquals

class CircularDependencyCalculatorTest {

    private val circularDependencyRepository = mock(CircularDependencyRepository::class.java)
    private val calculator = CircularDependencyCalculator(circularDependencyRepository)

    @Test
    fun `should return correct calculate result`() {
        val systemId = 1L

        `when`(
            circularDependencyRepository.getCircularDependencyBadSmellCalculateResult(
                systemId,
                CircularDependencyType.METHOD,
                calculator.getMethodCircularDependencyLevelRanges()
            )
        ).thenReturn(BadSmellLevel(level1 = 5, level2 = 3, level3 = 2))

        `when`(
            circularDependencyRepository.getCircularDependencyBadSmellCalculateResult(
                systemId,
                CircularDependencyType.CLASS,
                calculator.getClassCircularDependencyLevelRanges()
            )
        ).thenReturn(BadSmellLevel(level1 = 2, level2 = 4, level3 = 1))

        `when`(
            circularDependencyRepository.getCircularDependencyBadSmellCalculateResult(
                systemId,
                CircularDependencyType.PACKAGE,
                calculator.getPackageCircularDependencyLevelRanges()
            )
        ).thenReturn(BadSmellLevel(level1 = 1, level2 = 2, level3 = 3))

        `when`(
            circularDependencyRepository.getCircularDependencyBadSmellCalculateResult(
                systemId,
                CircularDependencyType.MODULE,
                calculator.getModuleCircularDependencyLevelRanges()
            )
        ).thenReturn(BadSmellLevel(level1 = 4, level2 = 1, level3 = 2))

        val actual = calculator.getCalculateResult(systemId)

        assertEquals(BadSmellLevel(level1 = 12, level2 = 10, level3 = 8), actual)
    }
}

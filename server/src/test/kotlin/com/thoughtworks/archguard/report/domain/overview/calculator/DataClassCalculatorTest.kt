package com.thoughtworks.archguard.report.domain.overview.calculator;

import com.thoughtworks.archguard.report.domain.redundancy.DataClassRepository
import org.archguard.smell.BadSmellLevel
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

class DataClassCalculatorTest {

    private val dataClassRepository = mock(DataClassRepository::class.java)
    private val dataClassCalculator = DataClassCalculator(dataClassRepository)

    @Test
    fun `should return BadSmellLevel with level1 when count is in range 0`() {
        // given
        val systemId = 1L
        val count = 20L
        `when`(dataClassRepository.getAllDataClassCount(systemId)).thenReturn(count)

        // when
        val result = dataClassCalculator.getCalculateResult(systemId)

        // then
        val expected = BadSmellLevel(1L, 0L, 0L)
        assertEquals(expected, result)
    }

    @Test
    fun `should return BadSmellLevel with level2 when count is in range 1`() {
        // given
        val systemId = 1L
        val count = 50L
        val range = dataClassCalculator.getTypeCountLevelRanges()
        `when`(dataClassRepository.getAllDataClassCount(systemId)).thenReturn(count)

        // when
        val result = dataClassCalculator.getCalculateResult(systemId)

        // then
        val expected = BadSmellLevel(0L, 1L, 0L)
        assertEquals(expected, result)
    }

    @Test
    fun `should return BadSmellLevel with level3 when count is in range 2`() {
        // given
        val systemId = 1L
        val count = 150L
        `when`(dataClassRepository.getAllDataClassCount(systemId)).thenReturn(count)

        // when
        val result = dataClassCalculator.getCalculateResult(systemId)

        // then
        val expected = BadSmellLevel(0L, 0L, 1L)
        assertEquals(expected, result)
    }

    @Test
    fun `should return BadSmellLevel with all levels as 0 when count is not in any range`() {
        // given
        val systemId = 1L
        val count = 5L
        `when`(dataClassRepository.getAllDataClassCount(systemId)).thenReturn(count)

        // when
        val result = dataClassCalculator.getCalculateResult(systemId)

        // then
        val expected = BadSmellLevel(0L, 0L, 0L)
        assertEquals(expected, result)
    }
}


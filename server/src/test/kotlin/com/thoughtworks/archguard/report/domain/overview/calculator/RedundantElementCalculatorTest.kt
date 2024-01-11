package com.thoughtworks.archguard.report.domain.overview.calculator;

import com.thoughtworks.archguard.report.domain.redundancy.DataClassRepository
import com.thoughtworks.archguard.report.domain.redundancy.RedundancyRepository
import org.archguard.smell.BadSmellLevel
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.Mockito.mock

class RedundantElementCalculatorTest {

    val redundancyRepository = mock(RedundancyRepository::class.java)
    val dataClassRepository = mock(DataClassRepository::class.java)

    @Test
    fun `should return correct BadSmellLevel when calculating redundant elements`() {
        // given
        val systemId = 1L
        val oneMethodClassCount = 20L
        val oneFieldClassCount = 40L

        val calculator = RedundantElementCalculator(redundancyRepository, dataClassRepository)

        given(redundancyRepository.getOneMethodClassCount(systemId)).willReturn(oneMethodClassCount)
        given(dataClassRepository.getAllDataClassWithOnlyOneFieldCount(systemId)).willReturn(oneFieldClassCount)

        // when
        val result = calculator.getCalculateResult(systemId)

        // then
        assertThat(result).isEqualTo(BadSmellLevel(1L, 1L, 0L))
    }
}

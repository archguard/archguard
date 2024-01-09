package com.thoughtworks.archguard.report.domain.redundancy;

import com.thoughtworks.archguard.report.domain.models.ClassVO
import org.archguard.model.vos.FieldVO
import org.archguard.smell.BadSmellType
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

class RedundancyServiceTest {
    private val redundancyRepository = mock(RedundancyRepository::class.java)
    private val dataClassRepository = mock(DataClassRepository::class.java)
    private val redundancyService = RedundancyService(redundancyRepository, dataClassRepository)

    @Test
    fun shouldReturnOneMethodClassWithTotalCount() {
        // given
        val systemId = 1L
        val limit = 10L
        val offset = 0L
        val expectedCount = 5L
        val expectedList = listOf(
            ClassVO("module", "package", "Class1"),
            ClassVO("module", "package", "Class2")
        )
        `when`(redundancyRepository.getOneMethodClassCount(systemId)).thenReturn(expectedCount)
        `when`(redundancyRepository.getOneMethodClass(systemId, limit, offset)).thenReturn(expectedList)

        // when
        val result = redundancyService.getOneMethodClassWithTotalCount(systemId, limit, offset)

        // then
        assertEquals(expectedCount to expectedList, result)
    }

    @Test
    fun shouldReturnOneFieldClassWithTotalCount() {
        // given
        val systemId = 1L
        val limit = 10L
        val offset = 0L
        val expectedCount = 3L
        val expectedList = listOf(
            DataClass("module", "package", "DataClass1", listOf(FieldVO("field1", "string"))),
            DataClass("module", "package", "DataClass2", listOf(FieldVO("field1", "string")))
        )
        `when`(dataClassRepository.getAllDataClassWithOnlyOneFieldCount(systemId)).thenReturn(expectedCount)
        `when`(dataClassRepository.getAllDataClassWithOnlyOneField(systemId, limit, offset)).thenReturn(expectedList)

        // when
        val result = redundancyService.getOneFieldClassWithTotalCount(systemId, limit, offset)

        // then
        assertEquals(expectedCount to expectedList, result)
    }

    @Test
    fun shouldReturnRedundantReport() {
        // given
        val systemId = 1L
        val expectedOneMethodCount = 5L
        val expectedOneFieldCount = 3L
        `when`(redundancyRepository.getOneMethodClassCount(systemId)).thenReturn(expectedOneMethodCount)
        `when`(dataClassRepository.getAllDataClassWithOnlyOneFieldCount(systemId)).thenReturn(expectedOneFieldCount)
        val expectedReport = mapOf(BadSmellType.REDUNDANT_ELEMENT to (expectedOneMethodCount + expectedOneFieldCount))

        // when
        val result = redundancyService.getRedundantReport(systemId)

        // then
        assertEquals(expectedReport, result)
    }
}

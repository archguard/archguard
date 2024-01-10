package com.thoughtworks.archguard.report.domain.cohesion;

import com.thoughtworks.archguard.report.domain.redundancy.DataClass
import com.thoughtworks.archguard.report.domain.redundancy.DataClassRepository
import org.archguard.smell.BadSmellType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.springframework.stereotype.Service

@Service
class DataClassServiceTest {

    private val dataClassRepository = mock(DataClassRepository::class.java)
    private val dataClassService = DataClassService(dataClassRepository)

    @Test
    fun shouldGetDataClassWithTotalCount() {
        // given
        val systemId = 1L
        val limit = 10L
        val offset = 0L
        val expectedCount = 100L
        val expectedDataClassList = listOf(
            DataClass("ModuleA", "com.example", "TestClass", listOf()),
            DataClass("ModuleB", "com.example", "TestClass", listOf())
        )

        `when`(dataClassRepository.getAllDataClassCount(systemId)).thenReturn(expectedCount)
        `when`(dataClassRepository.getAllDataClass(systemId, limit, offset)).thenReturn(expectedDataClassList)

        // when
        val result = dataClassService.getDataClassWithTotalCount(systemId, limit, offset)

        // then
        assertEquals(expectedCount to expectedDataClassList, result)
    }

    @Test
    fun shouldGetCohesionReport() {
        // given
        val systemId = 1L
        val expectedCount = 100L
        `when`(dataClassRepository.getAllDataClassCount(systemId)).thenReturn(expectedCount)

        // when
        val result = dataClassService.getCohesionReport(systemId)

        // then
        assertEquals(mapOf(BadSmellType.DATA_CLASS to expectedCount), result)
    }
}

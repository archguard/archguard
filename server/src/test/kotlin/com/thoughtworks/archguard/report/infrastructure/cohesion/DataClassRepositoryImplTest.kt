package com.thoughtworks.archguard.report.infrastructure.cohesion

import com.thoughtworks.archguard.report.domain.redundancy.DataClass
import org.archguard.model.vos.FieldVO
import org.jdbi.v3.core.Jdbi
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

class DataClassRepositoryImplTest {

    private val jdbi = mock(Jdbi::class.java)
    private val dataClassRepository = DataClassRepositoryImpl(jdbi)

    @Test
    fun `test getAllDataClassCount`() {
        val systemId = 1L
        val expectedCount = 10L

        `when`(jdbi.withHandle<Long, Exception>(any())).thenReturn(expectedCount)

        val actualCount = dataClassRepository.getAllDataClassCount(systemId)

        assertEquals(expectedCount, actualCount)
    }

    @Test
    fun `test getAllDataClassWithOnlyOneFieldCount`() {
        val systemId = 1L
        val expectedCount = 5L

        `when`(jdbi.withHandle<Long, Exception>(any())).thenReturn(expectedCount)

        val actualCount = dataClassRepository.getAllDataClassWithOnlyOneFieldCount(systemId)

        assertEquals(expectedCount, actualCount)
    }
}

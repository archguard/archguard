package com.thoughtworks.archguard.report.infrastructure.redundancy;

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.HandleCallback
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.result.ResultIterable
import org.jdbi.v3.core.statement.Query
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class RedundancyRepositoryImplTest {
    private val jdbi: Jdbi = mockk()
    private val repository = RedundancyRepositoryImpl(jdbi)

    @Test
    fun `getOneMethodClassCount should return correct count`() {
        val systemId = 1L
        val expectedCount = 5L

        val handle: Handle = mockk()
        val query: Query = mockk()
        val result: ResultIterable<Long> = mockk()

        every { jdbi.withHandle<Long, Exception>(any()) } answers {
            expectedCount
        }

        every { handle.createQuery(any()) } returns query
        every { query.bind("system_id", systemId) } returns query
        every { query.mapTo(Long::class.java) } returns result
        every { result.one() } returns expectedCount

        val actualCount = repository.getOneMethodClassCount(systemId)

        // Assert
        assertEquals(expectedCount, actualCount)
    }
}
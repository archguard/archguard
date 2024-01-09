package com.thoughtworks.archguard.report.infrastructure.cohesion;

import com.thoughtworks.archguard.report.domain.cohesion.ShotgunSurgery
import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.statement.Query
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import kotlin.io.path.Path

class ShotgunSurgeryRepositoryImplTest {

    @Test
    fun shouldReturnShotgunSurgeryCommitIds() {
        // Given
        val systemId = 1L
        val limit = 10
        val expectedCommitIds = listOf("commit1", "commit2", "commit3")
        val jdbi = mock(Jdbi::class.java)
        val repository = ShotgunSurgeryRepositoryImpl(jdbi)
        val handle = mock(Handle::class.java)
        val query = mock(Query::class.java)
        val result = listOf(
            CognitiveComplexityPO(1, "commit1"),
            CognitiveComplexityPO(2, "commit2"),
            CognitiveComplexityPO(3, "commit3")
        )

        `when`(jdbi.withHandle<List<CognitiveComplexityPO>, Exception>(any())).thenReturn(result)
        `when`(handle.createQuery(any())).thenReturn(query)

        // When
        val actualCommitIds = repository.getShotgunSurgeryCommitIds(systemId, limit)

        // Then
        assertEquals(expectedCommitIds, actualCommitIds)
    }
}

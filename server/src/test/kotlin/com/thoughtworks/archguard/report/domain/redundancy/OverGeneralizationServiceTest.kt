package com.thoughtworks.archguard.report.domain.redundancy;

import com.thoughtworks.archguard.report.domain.models.ClassVO
import org.archguard.smell.BadSmellType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

class OverGeneralizationServiceTest {

    @Test
    fun shouldReturnEmptyListWhenParentClassesIsEmpty() {
        // Given
        val systemId = 1L
        val limit = 10L
        val offset = 0L
        val overGeneralizationRepository = mock(OverGeneralizationRepository::class.java)
        val overGeneralizationService = OverGeneralizationService(overGeneralizationRepository)
        `when`(overGeneralizationRepository.getOverGeneralizationParentClassId(systemId)).thenReturn(emptyList())

        // When
        val result = overGeneralizationService.getOneExtendsWithTotalCount(systemId, limit, offset)

        // Then
        assertEquals(0L to emptyList<OverGeneralizationPair>(), result)
    }

    @Test
    fun shouldReturnPairListWhenParentClassesIsNotEmpty() {
        // Given
        val systemId = 1L
        val limit = 10L
        val offset = 0L
        val parentClasses = listOf("ParentClass1", "ParentClass2")
        val overGeneralizationRepository = mock(OverGeneralizationRepository::class.java)
        val overGeneralizationService = OverGeneralizationService(overGeneralizationRepository)
        `when`(overGeneralizationRepository.getOverGeneralizationParentClassId(systemId)).thenReturn(parentClasses)
        val pairList = listOf(
            OverGeneralizationPair(
                ClassVO("module", "package", "Class1"),
                ClassVO("module", "package", "Class2")
            ),
            OverGeneralizationPair(
                ClassVO("module", "package", "Class3"),
                ClassVO("module", "package", "Class4")
            )
        )
        `when`(overGeneralizationRepository.getOverGeneralizationPairList(parentClasses, limit, offset)).thenReturn(
            pairList
        )

        // When
        val result = overGeneralizationService.getOneExtendsWithTotalCount(systemId, limit, offset)

        // Then
        assertEquals(parentClasses.size.toLong() to pairList, result)
    }

    @Test
    fun shouldReturnRedundantReport() {
        // Given
        val systemId = 1L
        val overGeneralizationCount = 5L
        val overGeneralizationRepository = mock(OverGeneralizationRepository::class.java)
        val overGeneralizationService = OverGeneralizationService(overGeneralizationRepository)
        `when`(overGeneralizationRepository.getOverGeneralizationCount(systemId)).thenReturn(overGeneralizationCount)

        // When
        val result = overGeneralizationService.getRedundantReport(systemId)

        // Then
        assertEquals(mapOf(BadSmellType.OVER_GENERALIZATION to overGeneralizationCount), result)
    }
}

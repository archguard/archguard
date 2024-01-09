package org.archguard.threshold;

import org.archguard.model.ChangeEntry
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class CognitiveComplexityTest {

    @Test
    fun shouldCreateCognitiveComplexityObjectFromPathAndChangeEntryList() {
        // Given
        val path = "example/path"
        val changeEntryList = listOf(
            ChangeEntry("commit1",  "newPath1", 5, 0L, "commit1", "ADD"),
            ChangeEntry("commit2",  "newPath2", 3, 0L, "commit2", "ADD"),
            ChangeEntry("commit3",  "newPath3", 8, 0L, "commit3", "ADD")
        )
        val systemId = 123L

        // When
        val result = CognitiveComplexity.from(path, changeEntryList, systemId)

        // Then
        assertEquals(3, result.size)
        assertEquals("commit1", result[0].commitId)
        assertEquals(5, result[0].changedCognitiveComplexity)
        assertEquals(systemId, result[0].systemId)
        assertEquals("newPath1", result[0].path)
        assertEquals("commit2", result[1].commitId)
        assertEquals(2, result[1].changedCognitiveComplexity)
        assertEquals(systemId, result[1].systemId)
        assertEquals(path, result[1].path)
        assertEquals("commit3", result[2].commitId)
        assertEquals(5, result[2].changedCognitiveComplexity)
        assertEquals(systemId, result[2].systemId)
        assertEquals(path, result[2].path)
    }
}

package com.thoughtworks.archguard.change.infrastructure;

import com.thoughtworks.archguard.change.domain.repository.GitChangeRepository
import com.thoughtworks.archguard.change.domain.model.GitHotFilePO
import com.thoughtworks.archguard.change.domain.model.GitPathChangeCount
import com.thoughtworks.archguard.change.infrastructure.GitChangeDao
import com.thoughtworks.archguard.change.infrastructure.GitChangeRepositoryImpl
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

class GitChangeRepositoryImplTest {

    @Test
    fun `should return hot files by system id`() {
        // Given
        val systemId = 1L
        val expectedHotFiles = listOf(
            GitHotFilePO(1, "file1", "src/main", "root", "commit1", 2, "Main"),
            GitHotFilePO(2, "file2", "src/main", "root", "commit2", 3, "Main")
        )
        val gitChangeDao = mock(GitChangeDao::class.java)
        val gitChangeRepository: GitChangeRepository = GitChangeRepositoryImpl(gitChangeDao)
        `when`(gitChangeDao.findBySystemId(systemId)).thenReturn(expectedHotFiles)

        // When
        val actualHotFiles = gitChangeRepository.findBySystemId(systemId)

        // Then
        assertEquals(expectedHotFiles, actualHotFiles)
    }

    @Test
    fun `should return path change count by system id`() {
        // Given
        val systemId = 1L
        val expectedPathChangeCount = listOf(
            GitPathChangeCount(1, "path1", 10, 100),
            GitPathChangeCount(2, "path2", 5, 50)
        )
        val gitChangeDao = mock(GitChangeDao::class.java)
        val gitChangeRepository: GitChangeRepository = GitChangeRepositoryImpl(gitChangeDao)
        `when`(gitChangeDao.findCountBySystemId(systemId)).thenReturn(expectedPathChangeCount)

        // When
        val actualPathChangeCount = gitChangeRepository.findCountBySystemId(systemId)

        // Then
        assertEquals(expectedPathChangeCount, actualPathChangeCount)
    }

    @Test
    fun `should return unstable files by system id and size`() {
        // Given
        val systemId = 1L
        val size = 5L
        val topLines = listOf(
            GitPathChangeCount(1, "path1", 10, 200),
            GitPathChangeCount(2, "path2", 15, 100)
        )
        val topChanges = listOf(
            GitPathChangeCount(1, "path3", 10, 100),
            GitPathChangeCount(2, "path2", 15, 100)
        )
        val expectedUnstableFiles = listOf(
            GitPathChangeCount(2, "path2", 15, 100)
        )
        val gitChangeDao = mock(GitChangeDao::class.java)
        val gitChangeRepository: GitChangeRepository = GitChangeRepositoryImpl(gitChangeDao)
        `when`(gitChangeDao.getTopLinesFile(systemId, size)).thenReturn(topLines)
        `when`(gitChangeDao.getTopChangesFile(systemId, size)).thenReturn(topChanges)

        // When
        val actualUnstableFiles = gitChangeRepository.findUnstableFile(systemId, size)

        // Then
        assertEquals(expectedUnstableFiles, actualUnstableFiles)
    }

    @Test
    fun `should return changes by system id, start time, and end time`() {
        // Given
        val systemId = 1L
        val startTime = "2021-01-01"
        val endTime = "2021-01-31"
        val expectedChanges = listOf("change1", "change2", "change3")
        val gitChangeDao = mock(GitChangeDao::class.java)
        val gitChangeRepository: GitChangeRepository = GitChangeRepositoryImpl(gitChangeDao)
        `when`(gitChangeDao.findChangesByRange(systemId, startTime, endTime)).thenReturn(expectedChanges)

        // When
        val actualChanges = gitChangeRepository.findChangesByRange(systemId, startTime, endTime)

        // Then
        assertEquals(expectedChanges, actualChanges)
    }
}

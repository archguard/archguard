package com.thoughtworks.archguard.change.controller;

import com.thoughtworks.archguard.change.application.GitChangeApplicationService
import com.thoughtworks.archguard.change.domain.model.GitHotFilePO
import com.thoughtworks.archguard.change.domain.model.GitPathChangeCount
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension::class)
class GitChangeControllerTest {

    private lateinit var mockMvc: MockMvc

    @Mock
    private lateinit var gitChangeApplicationService: GitChangeApplicationService

    @BeforeEach
    fun setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(GitChangeController(
            gitChangeApplicationService
        )).build()
    }

    @Test
    fun shouldReturnGitHotFilesBySystemId() {
        // Given
        val systemId = 1L
        val gitHotFiles = listOf(
            GitHotFilePO(1, "file1", "src/main", "root", "commit1", 2, "Main"),
            GitHotFilePO(2, "file2", "src/main", "root", "commit2", 3, "Main")
        )
        given(gitChangeApplicationService.getGitHotFilesBySystemId(systemId)).willReturn(gitHotFiles)

        // When
        val result = mockMvc.perform(get("/api/systems/{systemId}/change/hot-files", systemId))
            .andExpect(status().isOk)
            .andReturn()

        // Then
        val response = result.response.contentAsString
        // Assert response content
    }

    @Test
    fun shouldReturnChangesInRange() {
        // Given
        val systemId = 1L
        val startTime = "2021-01-01"
        val endTime = "2021-01-31"
        val changes = listOf("commit1", "commit2")
        given(gitChangeApplicationService.getChangesByRange(systemId, startTime, endTime)).willReturn(changes)

        // When
        val result = mockMvc.perform(
            get("/api/systems/{systemId}/change/commit-ids", systemId)
                .param("startTime", startTime)
                .param("endTime", endTime)
        )
            .andExpect(status().isOk)
            .andReturn()

        // Then
        val response = result.response.contentAsString
        // Assert response content
    }

    @Test
    fun shouldReturnChangeCountByPath() {
        // Given
        val systemId = 1L
        val pathChangeCount = listOf(
            GitPathChangeCount(1, "path1", 10, 100),
            GitPathChangeCount(2, "path2", 5, 50)
        )
        given(gitChangeApplicationService.getPathChangeCount(systemId)).willReturn(pathChangeCount)

        // When
        val result = mockMvc.perform(get("/api/systems/{systemId}/change/path-change-count", systemId))
            .andExpect(status().isOk)
            .andReturn()

        // Then
        val response = result.response.contentAsString
        // Assert response content
    }

    @Test
    fun shouldReturnHighFrequencyChangeAndLongLines() {
        // Given
        val systemId = 1L
        val size = 50L
        val unstableFiles = listOf(
            GitPathChangeCount(1, "file1", 10, 100),
            GitPathChangeCount(2, "file2", 5, 50)
        )
        given(gitChangeApplicationService.getUnstableFile(systemId, size)).willReturn(unstableFiles)

        // When
        val result = mockMvc.perform(
            get("/api/systems/{systemId}/change/unstable-file", systemId)
                .param("size", size.toString())
        )
            .andExpect(status().isOk)
            .andReturn()
    }
}

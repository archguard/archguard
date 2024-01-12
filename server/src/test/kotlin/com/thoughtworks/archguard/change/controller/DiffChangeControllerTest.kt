package com.thoughtworks.archguard.change.controller;

import com.thoughtworks.archguard.change.application.DiffChangeApplicationService
import com.thoughtworks.archguard.change.domain.model.DiffChange
import com.thoughtworks.archguard.systeminfo.domain.RepoAuthType
import com.thoughtworks.archguard.systeminfo.domain.ScannedType
import com.thoughtworks.archguard.systeminfo.domain.SystemInfo
import com.thoughtworks.archguard.systeminfo.domain.SystemInfoService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension::class)
class DiffChangeControllerTest {
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var diffChangeApplicationService: DiffChangeApplicationService

    @MockBean
    private lateinit var systemInfoService: SystemInfoService

    @BeforeEach
    fun setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(DiffChangeController(
            diffChangeApplicationService,
            systemInfoService
        )).build()
    }

    @Test
    fun shouldReturnDiffChangesForHistoryInfluence() {
        val systemId = 1L
        val diffChanges = listOf(DiffChange(
            systemId = 1,
            sinceRev = "1.0",
            untilRev = "2.0",
            packageName = "com.example",
            className = "TestClass",
            relations = "A -> B, B -> C"
        ))

        given(diffChangeApplicationService.findBySystemId(systemId)).willReturn(diffChanges)

        mockMvc.perform(get("/api/systems/$systemId/diff/influence/history"))
            .andExpect(status().isOk)
    }

    @Test
    fun shouldExecuteDiffChangesForInfluenceByCommit() {
        val systemId = 1L
        val since = "2021-01-01"
        val until = "2021-01-31"
        val scannerVersion = "1.6.2"
        val systemInfo = SystemInfo(
            id = 1,
            systemName = "Test System",
            repo = "https://github.com/test/repo",
            username = "testuser",
            password = "testpassword",
            scanned = ScannedType.NONE,
            qualityGateProfileId = 123,
            repoType = "git",
            updatedTime = null,
            badSmellThresholdSuiteId = 456,
            branch = "master",
            language = "Java",
            codePath = "/src/main/java",
            workdir = "/path/to/workdir",
            repoAuthType = RepoAuthType.UsernameAndPassword,
            sshKeyString = "sshkey"
        )
        val diffChanges = listOf(DiffChange(
            systemId = 1,
            sinceRev = "1.0",
            untilRev = "2.0",
            packageName = "com.example",
            className = "TestClass",
            relations = "A -> B, B -> C"
        ))

        given(systemInfoService.getSystemInfo(systemId)).willReturn(systemInfo)
        given(diffChangeApplicationService.findBySystemId(systemId)).willReturn(diffChanges)

        mockMvc.perform(
            get("/api/systems/$systemId/diff/influence/commit")
                .param("since", since)
                .param("until", until)
                .param("scannerVersion", scannerVersion)
        )
            .andExpect(status().isOk)

        verify(diffChangeApplicationService).execute(systemInfo, since, until, scannerVersion)
    }
}

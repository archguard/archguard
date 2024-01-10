package com.thoughtworks.archguard.systeminfo.controller;

import com.thoughtworks.archguard.systeminfo.controller.dto.SystemInfoDTO
import com.thoughtworks.archguard.systeminfo.domain.RepoAuthType
import com.thoughtworks.archguard.systeminfo.domain.ScannedType
import com.thoughtworks.archguard.systeminfo.domain.SystemInfo
import com.thoughtworks.archguard.systeminfo.domain.SystemInfoService
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders

@ExtendWith(MockitoExtension::class)
class SystemInfoControllerTest {

    @Mock
    private lateinit var systemInfoService: SystemInfoService

    private lateinit var mockMvc: MockMvc

    @BeforeEach
    fun setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(
            SystemInfoController(
                zipFilePath = "",
                systemInfoService = systemInfoService,
                systemInfoMapper = SystemInfoMapper()
            )
        ).build()
    }

    @Test
    fun shouldReturnSystemInfoWhenGetSystemInfoById() {
        // Given
        val systemInfoId = 1L
        val systemInfo = SystemInfo(
            id = 1,
            systemName = "Test System",
            repo = "https://github.com/test/repo.git",
            username = "testuser",
            password = "testpassword",
            scanned = ScannedType.NONE,
            qualityGateProfileId = null,
            repoType = "git",
            updatedTime = null,
            badSmellThresholdSuiteId = 2,
            branch = "master",
            language = "Java",
            codePath = "/src/main/java",
            workdir = "/tmp/workdir",
            repoAuthType = RepoAuthType.UsernameAndPassword,
            sshKeyString = null
        )

        val expectedSystemInfoDTO = SystemInfoDTO(
            id = 1,
            systemName = "Test System",
            repo = listOf("https://github.com/test/repo.git"),
            username = "testuser",
            password = "testpassword",
            scanned = ScannedType.NONE,
            repoType = "git",
            updatedTime = null,
            badSmellThresholdSuiteId = 2,
            branch = "master",
            language = "Java",
            codePath = "/src/main/java"
        )

        given(systemInfoService.getSystemInfo(systemInfoId)).willReturn(systemInfo)

        // When
        val result = mockMvc.perform(get("/api/system-info/{id}", systemInfoId))
            .andExpect(status().isOk)
            .andReturn()

        // Then
        assertEquals(Json.decodeFromString<SystemInfoDTO>(result.response.contentAsString), expectedSystemInfoDTO)
    }
}

package com.thoughtworks.archguard.systeminfo.controller;

import com.thoughtworks.archguard.systeminfo.controller.dto.SystemInfoDTO
import com.thoughtworks.archguard.systeminfo.domain.RepoAuthType
import com.thoughtworks.archguard.systeminfo.domain.ScannedType
import com.thoughtworks.archguard.systeminfo.domain.SystemInfo
import com.thoughtworks.archguard.systeminfo.domain.SystemInfoService
import io.mockk.every
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.Mockito.doNothing
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
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

    @Test
    fun shouldReturnFilePathWhenUploadZipFile() {
        val file = MockMultipartFile("file", "test.zip", "application/zip", "test data".toByteArray())

        mockMvc.perform(
            MockMvcRequestBuilders.multipart("/api/system-info/upload")
                .file(file)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
        // test will fail in Windows
//            .andExpect(MockMvcResultMatchers.content().string("null"))
    }

    @Test
    fun shouldReturnErrorMessageWhenUploadEmptyZipFile() {
        val file = MockMultipartFile("file", "test.zip", "application/zip", ByteArray(0))

        mockMvc.perform(
            MockMvcRequestBuilders.multipart("/api/system-info/upload")
                .file(file)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().string("upload failed, please select file"))
    }

    // test for, getAllSystemInfo,updateSystemInfo,,addSystemInfo,deleteSystemInfo
    // test for getAllSystemInfo
    @Test
    fun shouldReturnAllSystemInfo() {
        // Given
        val systemInfoList = listOf(
            SystemInfo(
                id = 1,
                systemName = "Test System 1",
                repo = "https://github.com/test/repo1.git",
                username = "testuser1",
                password = "testpassword1",
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
            ),
            SystemInfo(
                id = 2,
                systemName = "Test System 2",
                repo = "https://github.com/test/repo2.git",
                username = "testuser2",
                password = "testpassword2",
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
        )

        val expectedSystemInfoDTOList = listOf(
            SystemInfoDTO(
                id = 1,
                systemName = "Test System 1",
                repo = listOf("https://github.com/test/repo1.git"),
                username = "testuser1",
                password = "testpassword1",
                scanned = ScannedType.NONE,
                repoType = "git",
                updatedTime = null,
                badSmellThresholdSuiteId = 2,
                branch = "master",
                language = "Java",
                codePath = "/src/main/java"
            ),
            SystemInfoDTO(
                id = 2,
                systemName = "Test System 2",
                repo = listOf("https://github.com/test/repo2.git"),
                username = "testuser2",
                password = "testpassword2",
                scanned = ScannedType.NONE,
                repoType = "git",
                updatedTime = null,
                badSmellThresholdSuiteId = 2,
                branch = "master",
                language = "Java",
                codePath = "/src/main/java"
            )
        )

        given(systemInfoService.getAllSystemInfo()).willReturn(systemInfoList)

        // When
        val result = mockMvc.perform(get("/api/system-info"))
            .andExpect(status().isOk)
            .andReturn()

        // Then
        assertEquals(
            Json.decodeFromString<List<SystemInfoDTO>>(result.response.contentAsString),
            expectedSystemInfoDTOList
        )
    }

    @Test
    fun shouldDeleteSystemInfo() {
        // Given
        val systemInfoId = 1L

        // When
        val result = mockMvc.perform(MockMvcRequestBuilders.delete("/api/system-info/{id}", systemInfoId))
            .andExpect(status().isOk)
            .andReturn()

        // Then
        assertEquals(result.response.contentAsString, "")
    }
}

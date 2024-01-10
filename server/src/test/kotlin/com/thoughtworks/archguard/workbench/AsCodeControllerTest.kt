package com.thoughtworks.archguard.workbench;

import com.fasterxml.jackson.databind.ObjectMapper
import com.thoughtworks.archguard.scanner.domain.scanner.javaext.bs.ScanContext
import com.thoughtworks.archguard.scanner.domain.system.BuildTool
import com.thoughtworks.archguard.scanner.infrastructure.command.InMemoryConsumer
import com.thoughtworks.archguard.workbench.model.AsCodeResponse
import com.thoughtworks.archguard.workbench.AacCodeDto
import com.thoughtworks.archguard.workbench.AsCodeScanDTO
import com.thoughtworks.archguard.systeminfo.domain.SystemInfoService
import com.thoughtworks.archguard.workbench.domain.AasDslRepository
import com.thoughtworks.archguard.smartscanner.StranglerScannerExecutor
import com.thoughtworks.archguard.systeminfo.domain.SystemInfo
import com.thoughtworks.archguard.workbench.domain.AacDslCodeModel
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import java.io.File
import java.util.*
import javax.annotation.Resource

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class AsCodeControllerTest {
    private var mockMvc: MockMvc? = null

    @MockBean
    private lateinit var systemInfoService: SystemInfoService

    @MockBean
    private lateinit var aacDslRepository: AasDslRepository

    @MockBean
    private lateinit var executor: StranglerScannerExecutor

    @Resource
    private lateinit var webApplicationContext: WebApplicationContext

    @BeforeEach
    fun setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    fun should_create_repos() {
        // given
        val repos = listOf(
            AsCodeRepositoryDTO("repo1", "url1", "language1"),
            AsCodeRepositoryDTO("repo2", "url2", "language2")
        )
        val systemInfo1 = SystemInfo(
            systemName = "repo1",
            repo = "url1",
            language = "language1",
            badSmellThresholdSuiteId = 1,
            branch = "master",
            repoType = "GIT",
            username = "",
            password = "",
            codePath = ""
        )
        val systemInfo2 = SystemInfo(
            systemName = "repo2",
            repo = "url2",
            language = "language2",
            badSmellThresholdSuiteId = 1,
            branch = "master",
            repoType = "GIT",
            username = "",
            password = "",
            codePath = ""
        )
        `when`(systemInfoService.addSystemInfo(systemInfo1)).thenReturn(1L)
        `when`(systemInfoService.addSystemInfo(systemInfo2)).thenReturn(2L)

        // when
        val result = mockMvc!!.perform(
            put("/api/ascode/repos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(repos.toJson())
        )

        // then
        result.andExpect(status().isOk)
            .andExpect(content().json(
                """
                {"content":{"success":["repo1","repo2"],"existed":[]}}
                """
            ))
    }

    @Test
    fun should_save_code() {
        // given
        val dto = AacCodeDto("code")
        val id = 1L
        `when`(aacDslRepository.getById(id)).thenReturn(AacDslCodeModel(id, content = "old code"))
        `when`(aacDslRepository.update(AacDslCodeModel(id, content = dto.code))).thenReturn(1)

        // when
        val result = mockMvc!!.perform(
            put("/api/ascode/dsl-code/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(dto.toJson())
        )

        // then
        result.andExpect(status().isOk)
            .andExpect(content().string(id.toString()))
    }

    @Test
    fun should_save_code_when_id_not_found() {
        // given
        val dto = AacCodeDto("code")
        val id = 1L
        `when`(aacDslRepository.getById(id)).thenReturn(null)
        `when`(aacDslRepository.save(AacDslCodeModel(id, content = dto.code))).thenReturn(id)

        // when
        val result = mockMvc!!.perform(
            put("/api/ascode/dsl-code/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(dto.toJson())
        )

        // then
        result.andExpect(status().isOk)
            .andExpect(content().string(id.toString()))
    }

    @Test
    fun should_get_code() {
        // given
        val id = 1L
        val codeModel = AacDslCodeModel(id, content = "code")
        `when`(aacDslRepository.getById(id)).thenReturn(codeModel)

        // when
        val result = mockMvc!!.perform(get("/api/ascode/dsl-code/{id}", id))

        // then
        result.andExpect(status().isOk)
            .andExpect(content().json(codeModel.toJson()))
    }

    @Test
    fun should_return_null_when_code_not_found() {
        // given
        val id = 1L
        `when`(aacDslRepository.getById(id)).thenReturn(null)

        // when
        val result = mockMvc!!.perform(get("/api/ascode/dsl-code/{id}", id))

        // then
        result.andExpect(status().isOk)
            .andExpect(content().string(""))
    }

    @Test
    fun should_create_scan() {
        // given
        val scanInfo = AsCodeScanDTO(
            name = "repo1",
            features = emptyList(),
            branch = "master",
            languages = emptyList(),
            specs = emptyList()
        )
        val systemInfo = SystemInfo(
            id = 1L,
            systemName = "repo1",
            repo = "url1",
            language = "language1",
            badSmellThresholdSuiteId = 1,
            branch = "master",
            repoType = "GIT",
            username = "",
            password = "",
            codePath = "",
            workdir = "/path/to/workdir"
        )
        val scanContext = ScanContext(
            systemId = systemInfo.id!!,
            repo = systemInfo.repo,
            buildTool = BuildTool.NONE,
            workspace = File(systemInfo.workdir),
            dbUrl = "",
            config = listOf(),
            language = systemInfo.language,
            codePath = systemInfo.codePath,
            branch = systemInfo.branch,
            logStream = InMemoryConsumer(),
            scannerVersion = "1.6.2",
            additionArguments = listOf()
        )
        `when`(systemInfoService.getSystemInfoByName(scanInfo.name)).thenReturn(systemInfo)
        doNothing().`when`(executor).execute(scanContext)

        // when
        val result = mockMvc!!.perform(
            put("/api/ascode/scan")
                .contentType(MediaType.APPLICATION_JSON)
                .param("scannerVersion", "1.6.2")
                .content(scanInfo.toJson())
        )

        // then
        result.andExpect(status().isOk)
            .andExpect(content().json(
                """
                {
                    "content": {}
                }
                """
            ))
    }

    private fun Any.toJson(): String = ObjectMapper().writeValueAsString(this)
}

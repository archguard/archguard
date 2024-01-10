package com.thoughtworks.archguard.report.controller;

import com.thoughtworks.archguard.report.controller.coupling.FilterSizingDto
import com.thoughtworks.archguard.report.domain.coupling.circulardependency.CircularDependencyService
import com.thoughtworks.archguard.report.domain.coupling.hub.*
import io.mockk.MockKAnnotations
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.archguard.json.JsonUtils
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class HubControllerTest {

    @MockBean
    private lateinit var hubService: HubService

    private lateinit var mockMvc: MockMvc


    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)
        mockMvc = MockMvcBuilders.standaloneSetup(HubController(hubService)).build()
    }

    @Test
    fun shouldReturnClassHubListDtoWhenGetClassesAboveHubThreshold() {
        val systemId = 1L
        val filterSizing = FilterSizingDto(1, 10, "", "", "", "")
        val orderByFanIn = false

        val limit = filterSizing.getLimit()
        val offset = filterSizing.getOffset()

        val classCouplingList = listOf(
            ClassCoupling(
                id = "1",
                moduleName = "ModuleA",
                packageName = "com.example.packageA",
                typeName = "ClassA",
                fanIn = 5,
                fanOut = 3,
                coupling = 0.6,
                instability = 0.4
            )
        )
        val count = 1L
        val threshold = 1

        given(hubService.getClassHubListAboveThreshold(systemId, limit, offset, orderByFanIn))
            .willReturn(Triple(classCouplingList, count, threshold))

        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/systems/1/hub/classes/above-threshold", systemId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtils.obj2json(filterSizing))
                .param("orderByFanIn", orderByFanIn.toString())
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.data").exists())
            .andExpect(jsonPath("$.count").value(count))
            .andExpect(jsonPath("$.currentPageNumber").value(offset / limit + 1))
            .andExpect(jsonPath("$.threshold").value(threshold))
    }

    @Test
    fun shouldReturnMethodHubListDtoWhenGetMethodsAboveHubThreshold() {
        val systemId = 1L
        val filterSizing = FilterSizingDto(1, 10, null, null, null, null)
        val orderByFanIn = false

        val limit = filterSizing.getLimit()
        val offset = filterSizing.getOffset()

        val methodCouplingList = listOf(
            MethodCoupling(
                id = "1",
                moduleName = "Module1",
                packageName = "com.example",
                typeName = "TestClass",
                methodName = "testMethod",
                args = "arg1, arg2",
                fanIn = 5,
                fanOut = 3,
                coupling = 0.6,
                instability = 0.4
            )
        )
        val count = 1L
        val threshold = 1

        given(hubService.getMethodHubListAboveThreshold(systemId, limit, offset, orderByFanIn))
            .willReturn(Triple(methodCouplingList, count, threshold))

        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/systems/1/hub/methods/above-threshold", systemId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtils.obj2json(filterSizing))
                .param("orderByFanIn", orderByFanIn.toString())
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.data").exists())
            .andExpect(jsonPath("$.count").value(count))
            .andExpect(jsonPath("$.currentPageNumber").value(offset / limit + 1))
            .andExpect(jsonPath("$.threshold").value(threshold))
    }

    @Test
    fun shouldReturnPackageHubListDtoWhenGetPackagesAboveHubThreshold() {
        val systemId = 1L
        val filterSizing = FilterSizingDto(1, 10, null, null, null, null)
        val orderByFanIn = false

        val limit = filterSizing.getLimit()
        val offset = filterSizing.getOffset()

        val packageCouplingList = listOf(
            PackageCoupling(
                id = "1",
                fanIn = 1,
                fanOut = 1,
                coupling = 1.0,
                instability = 1.0,
                moduleName = "module",
                packageName = "package"
            )
        )
        val count = 1L
        val threshold = 1

        given(hubService.getPackageHubListAboveThreshold(systemId, limit, offset, orderByFanIn))
            .willReturn(Triple(packageCouplingList, count, threshold))

        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/systems/1/hub/packages/above-threshold", systemId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtils.obj2json(filterSizing))
                .param("orderByFanIn", orderByFanIn.toString())
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.data").exists())
            .andExpect(jsonPath("$.count").value(count))
            .andExpect(jsonPath("$.currentPageNumber").value(offset / limit + 1))
            .andExpect(jsonPath("$.threshold").value(threshold))
    }

    @Test
    fun shouldReturnModuleHubListDtoWhenGetModulesAboveHubThreshold() {
        val systemId = 1L
        val filterSizing = FilterSizingDto(1, 10, null, null, null, null)
        val orderByFanIn = false

        val limit = filterSizing.getLimit()
        val offset = filterSizing.getOffset()

        val moduleCouplingList = listOf(
            ModuleCoupling(
                id = "1",
                fanIn = 1,
                fanOut = 1,
                coupling = 1.0,
                instability = 1.0,
                moduleName = "module"
            )
        )
        val count = 1L
        val threshold = 1

        given(hubService.getModuleHubListAboveThreshold(systemId, limit, offset, orderByFanIn))
            .willReturn(Triple(moduleCouplingList, count, threshold))

        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/systems/1/hub/modules/above-threshold", systemId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtils.obj2json(filterSizing))
                .param("orderByFanIn", orderByFanIn.toString())
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.data").exists())
            .andExpect(jsonPath("$.count").value(count))
            .andExpect(jsonPath("$.currentPageNumber").value(offset / limit + 1))
            .andExpect(jsonPath("$.threshold").value(threshold))
    }
}

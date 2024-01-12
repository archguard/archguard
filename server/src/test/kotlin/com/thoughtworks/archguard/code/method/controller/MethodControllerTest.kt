package com.thoughtworks.archguard.code.method.controller

import com.thoughtworks.archguard.code.method.domain.MethodService
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.archguard.model.code.JMethod
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension::class)
class MethodControllerTest {

    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var methodService: MethodService

    @BeforeEach
    fun setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(MethodController(methodService)).build()
    }

    @Test
    fun shouldReturnMethodCallees() {
        // Given
        val systemId = 1L
        val methodName = "testMethod"
        val clazzName = "TestClass"
        val deep = 3
        val needIncludeImpl = true
        val moduleName = "TestModule"
        val jMethodList = listOf(JMethod("1", "methodName", "TestClass", null, "void", listOf("String")))

        given(methodService.findMethodCallees(systemId, moduleName, clazzName, methodName, deep, needIncludeImpl))
            .willReturn(jMethodList)

        // When
        mockMvc.perform(
            get("/api/systems/{systemId}/methods/callees", systemId)
                .param("name", methodName)
                .param("clazz", clazzName)
                .param("deep", deep.toString())
                .param("needIncludeImpl", needIncludeImpl.toString())
                .param("module", moduleName)
        )
            // Then
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().json(Json.encodeToString(jMethodList)))
    }

    @Test
    fun shouldReturnMethodCallers() {
        // Given
        val systemId = 1L
        val methodName = "testMethod"
        val clazzName = "TestClass"
        val deep = 3
        val moduleName = "TestModule"
        val jMethodList = listOf(JMethod("1", "methodName", "TestClass", null, "void", listOf("String")))

        given(methodService.findMethodCallers(systemId, moduleName, clazzName, methodName, deep))
            .willReturn(jMethodList)

        // When
        mockMvc.perform(
            get("/api/systems/{systemId}/methods/callers", systemId)
                .param("name", methodName)
                .param("clazz", clazzName)
                .param("deep", deep.toString())
                .param("module", moduleName)
        )
            // Then
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().json(Json.encodeToString(jMethodList)))
    }

    @Test
    fun shouldReturnMethodInvokes() {
        // Given
        val systemId = 1L
        val methodName = "testMethod"
        val clazzName = "TestClass"
        val deep = 3
        val callerDeep = 2
        val calleeDeep = 2
        val needIncludeImpl = true
        val moduleName = "TestModule"
        val jMethodList = listOf(JMethod("1", "methodName", "TestClass", null, "void", listOf("String")))

        given(
            methodService.findMethodInvokes(
                systemId, moduleName, clazzName, methodName,
                callerDeep, calleeDeep, needIncludeImpl
            )
        )
            .willReturn(jMethodList)

        // When
        mockMvc.perform(
            get("/api/systems/{systemId}/methods/invokes", systemId)
                .param("name", methodName)
                .param("clazz", clazzName)
                .param("deep", deep.toString())
                .param("callerDeep", callerDeep.toString())
                .param("calleeDeep", calleeDeep.toString())
                .param("needIncludeImpl", needIncludeImpl.toString())
                .param("module", moduleName)
        )
            // Then
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().json(Json.encodeToString(jMethodList)))
    }

    @Test
    fun shouldReturnMethodsBelongToClass() {
        // Given
        val systemId = 1L
        val clazzName = "TestClass"
        val submoduleName = "TestSubmodule"
        val jMethodList = listOf(JMethod("1", "methodName", "TestClass", null, "void", listOf("String")))

        given(methodService.findMethodByModuleAndClazz(systemId, clazzName, submoduleName))
            .willReturn(jMethodList)

        // When
        mockMvc.perform(
            get("/api/systems/{systemId}/methods", systemId)
                .param("clazz", clazzName)
                .param("submodule", submoduleName)
        )
            // Then
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().json(Json.encodeToString(jMethodList)))
    }
}

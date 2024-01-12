package com.thoughtworks.archguard.code.module.controller;

import com.thoughtworks.archguard.code.module.domain.LogicModuleRepository
import org.archguard.arch.LogicModule
import org.archguard.arch.SubModule
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension::class)
class BaseModuleControllerTest {

    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var logicModuleRepository: LogicModuleRepository

    @BeforeEach
    fun setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(BaseModuleController(logicModuleRepository)).build()
    }

    @Test
    fun shouldReturnBaseModules() {
        val systemId = 1L
        val subModules = listOf(SubModule("subModule1"), SubModule("subModule2"))

        given(logicModuleRepository.getAllSubModule(systemId)).willReturn(subModules)

        mockMvc.perform(get("/api/systems/$systemId/base-modules"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0]").value("subModule1"))
            .andExpect(jsonPath("$[1]").value("subModule2"))
    }
}

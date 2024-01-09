package com.thoughtworks.archguard.architecture.controller

import com.thoughtworks.archguard.architecture.application.request.ArchSystemCreateRequest
import com.thoughtworks.archguard.architecture.domain.repository.ArchSystemRepository
import org.archguard.json.JsonUtils
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import javax.annotation.Resource


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class ArchSystemControllerTest {
    private var mockMvc: MockMvc? = null

    @Resource
    private lateinit var webApplicationContext: WebApplicationContext

    @Autowired
    private lateinit var archSystemRepository: ArchSystemRepository

    @BeforeEach
    fun setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    fun should_create_arch_system() {
        val request = ArchSystemCreateRequest("any")

        mockMvc!!.perform(
            MockMvcRequestBuilders.post("/api/arch-systems")
                .contentType(APPLICATION_JSON)
                .content(JsonUtils.obj2json(request))
        )
            .andExpect(MockMvcResultMatchers.status().isCreated)

        val archSystemList = archSystemRepository.findAll()
        Assertions.assertThat(archSystemList).hasSize(1)
    }
}
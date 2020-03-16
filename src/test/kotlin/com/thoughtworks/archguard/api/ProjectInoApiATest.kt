package com.thoughtworks.archguard.api

import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.TestMethodOrder
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpMethod
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import javax.annotation.Resource

@SpringBootTest
@WebAppConfiguration
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class ProjectInoApiATest {

    @Resource
    private lateinit var wac: WebApplicationContext

    @Test
    @Order(1)
    fun should_get_project_info_when_sent_get_project_info_api_given_there_is_already_project_info_in_database() {
        val request = MockMvcRequestBuilders.request(HttpMethod.GET, "/project/info")
        val result = MockMvcBuilders.webAppContextSetup(wac).build().perform(request)
                .andExpect(status().isOk)
                .andReturn()

        val content = result.response.contentAsString
        val status = result.response.contentAsString

        val except = "{\"id\":\"c06da91f-6742-11ea-8188-0242ac110002\",\"project_name\":\"spring\",\"git_repo\":\"https://github.com/spring-projects/spring-framework.git\"}"

        assertEquals(200, status)
        assertEquals(except, content)
    }


}
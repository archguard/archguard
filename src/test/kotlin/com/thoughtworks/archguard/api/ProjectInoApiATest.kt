package com.thoughtworks.archguard.api

import org.jdbi.v3.core.Jdbi
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.springframework.beans.factory.annotation.Autowired
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

    @Autowired
    lateinit var jdbi: Jdbi

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
        val status = result.response.status

        val except = "{\"id\":\"c06da91f-6742-11ea-8188-0242ac110002\",\"projectName\":\"spring\",\"gitRepo\":[\"https://github.com/spring-projects/spring-framework.git\"]}"

        assertEquals(200, status)
        assertEquals(except, content)
    }

    @Test
    @Order(2)
    fun should_get_success_message_when_sent_update_project_info_api_given_there_is_already_project_info_in_database() {
        val request = MockMvcRequestBuilders.request(HttpMethod.PUT, "/project/info")
                .contentType("application/json")
                .content("{\"id\":\"c06da91f-6742-11ea-8188-0242ac110002\",\"projectName\":\"spring1\",\"gitRepo\":[\"https://github.com/spring-projects/spring-framework.git\"]}")
        val result = MockMvcBuilders.webAppContextSetup(wac).build().perform(request)
                .andExpect(status().isOk)
                .andReturn()

        val content = result.response.contentAsString
        val status = result.response.status
        val except = "{\"success\":true,\"message\":\"update project info success\"}"

        assertEquals(200, status)
        assertEquals(except, content)

        val re = jdbi.withHandle<String, Nothing> {
            it.createQuery("select `name` from ProjectInfo where id = 'c06da91f-6742-11ea-8188-0242ac110002'")
                    .mapTo(String::class.java)
                    .one()
        }

        assertEquals("spring1", re)
    }

    @Test
    @Order(3)
    fun should_get_exists_massage_when_sent_add_project_info_api_given_there_is_already_project_info_in_database() {
        val request = MockMvcRequestBuilders.request(HttpMethod.POST, "/project/info")
                .contentType("application/json")
                .content("{\"projectName\":\"spring\",\"gitRepo\":[\"https://github.com/spring-projects/spring-framework.git\"]}")
        val result = MockMvcBuilders.webAppContextSetup(wac).build().perform(request)
                .andExpect(status().isOk)
                .andReturn()

        val content = result.response.contentAsString
        val status = result.response.status
        val except = "{\"success\":false,\"message\":\"There is already project info\",\"id\":\"null\"}"

        assertEquals(200, status)
        assertEquals(except, content)
    }

    @Test
    @Order(4)
    fun should_get_success_massage_when_sent_add_project_info_api_given_there_is_no_project_info_in_database() {
        jdbi.withHandle<Int, Nothing> {
            it.createUpdate("delete from ProjectInfo")
                    .execute()
        }
        val request = MockMvcRequestBuilders.request(HttpMethod.POST, "/project/info")
                .contentType("application/json")
                .content("{\"projectName\":\"spring\",\"gitRepo\":[\"https://github.com/spring-projects/spring-framework.git\"]}")
        val result = MockMvcBuilders.webAppContextSetup(wac).build().perform(request)
                .andExpect(status().isOk)
                .andReturn()

        val content = result.response.contentAsString
        val status = result.response.status

        assertEquals(200, status)

        val re = jdbi.withHandle<List<String>, Nothing> {
            it.createQuery("select id from ProjectInfo where `name` = 'spring'")
                    .mapTo(String::class.java)
                    .list()
        }
        assertEquals(1, re.size)

        val except = "{\"success\":true,\"message\":\"add new project info success\",\"id\":\"${re[0]}\"}"
        assertEquals(except, content)

    }
}
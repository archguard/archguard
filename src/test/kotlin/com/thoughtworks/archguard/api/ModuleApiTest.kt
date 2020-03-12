package com.thoughtworks.archguard.api

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpMethod
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import javax.annotation.Resource


@SpringBootTest
@WebAppConfiguration
class ModuleApiTest {
    @Resource
    private lateinit var wac: WebApplicationContext

    @Test
    fun should_get_package_dependence_when_send_package_dependency_api() {
        val request = MockMvcRequestBuilders.request(HttpMethod.GET, "/module/dependence/all")
        val result = MockMvcBuilders.webAppContextSetup(wac).build().perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andReturn()

        val content = result.response.contentAsString
        val status = result.response.status

        val except = "{\"nodes\":[{\"id\":1,\"name\":\"web\"},{\"id\":2,\"name\":\"core\"}],\"edges\":[{\"a\":1,\"b\":2,\"num\":15}]}"
        Assertions.assertEquals(200, status)
        Assertions.assertEquals(except, content)
    }
}
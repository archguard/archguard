package com.thoughtworks.archguard.archguardaddition.api

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
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
class PackageApiTest {

    @Resource
    private lateinit var wac: WebApplicationContext

    @Test
    fun should_get_hello_world_when_send_hello_api() {
        val request = MockMvcRequestBuilders.request(HttpMethod.GET, "/hello")
        val result = MockMvcBuilders.webAppContextSetup(wac).build().perform(request)
                .andExpect(status().isOk)
                .andReturn()

        val content = result.response.contentAsString
        val status = result.response.status

        assertEquals(200, status)
        assertEquals("Hello World!", content)
    }

}
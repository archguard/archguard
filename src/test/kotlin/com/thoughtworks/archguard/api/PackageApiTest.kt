package com.thoughtworks.archguard.api

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
    fun should_get_package_dependence_when_send_package_dependency_api() {
        val request = MockMvcRequestBuilders.request(HttpMethod.GET, "/package/dependence/all")
        val result = MockMvcBuilders.webAppContextSetup(wac).build().perform(request)
                .andExpect(status().isOk)
                .andReturn()

        val content = result.response.contentAsString
        val status = result.response.status

        val except = "{\"nodes\":[{\"id\":1,\"name\":\"org\",\"parent\":0},{\"id\":2,\"name\":\"springframework\",\"parent\":1},{\"id\":3,\"name\":\"core\",\"parent\":2},{\"id\":4,\"name\":\"annotation\",\"parent\":3},{\"id\":5,\"name\":\"env\",\"parent\":3},{\"id\":6,\"name\":\"io\",\"parent\":3},{\"id\":7,\"name\":\"support\",\"parent\":6},{\"id\":8,\"name\":\"task\",\"parent\":3},{\"id\":9,\"name\":\"web\",\"parent\":2},{\"id\":10,\"name\":\"context\",\"parent\":9},{\"id\":11,\"name\":\"request\",\"parent\":10},{\"id\":12,\"name\":\"async\",\"parent\":11},{\"id\":13,\"name\":\"support\",\"parent\":10}],\"edges\":[{\"a\":10,\"b\":3,\"num\":1},{\"a\":10,\"b\":4,\"num\":1},{\"a\":10,\"b\":7,\"num\":1},{\"a\":12,\"b\":8,\"num\":1},{\"a\":13,\"b\":5,\"num\":7},{\"a\":13,\"b\":6,\"num\":1},{\"a\":13,\"b\":7,\"num\":3}]}"
        assertEquals(200, status)
        assertEquals(except, content)
    }
}
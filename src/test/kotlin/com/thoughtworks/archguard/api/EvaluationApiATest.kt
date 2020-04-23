package com.thoughtworks.archguard.api

import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.Jdbi
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
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
class EvaluationApiATest {

    @Autowired
    lateinit var jdbi: Jdbi

    @Resource
    private lateinit var wac: WebApplicationContext

    @Test
    fun should_generate_evaluation() {
        val request = MockMvcRequestBuilders.request(HttpMethod.POST, "/quality-evaluations")
        val result = MockMvcBuilders.webAppContextSetup(wac).build().perform(request)
                .andExpect(status().isOk)
                .andReturn()

        val content = result.response.contentAsString

        val idSaved = jdbi.withHandle<String, RuntimeException> { handle: Handle ->
            handle.createQuery("select id from evaluationReport")
                    .mapTo(String::class.java).one()
        }
        assertEquals(idSaved, content)
    }
}
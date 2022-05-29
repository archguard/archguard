package com.thoughtworks.archguard.api

import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.Jdbi
import org.json.JSONArray
import org.json.JSONObject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpMethod
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import javax.annotation.Resource

@Disabled
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class EvaluationApiATest {

    @Autowired
    lateinit var jdbi: Jdbi

    @Resource
    private lateinit var wac: WebApplicationContext

    @Test
    @Order(1)
    fun should_generate_evaluation() {
        val request = MockMvcRequestBuilders.request(HttpMethod.POST, "/quality-evaluations")
        val result = MockMvcBuilders.webAppContextSetup(wac).build().perform(request)
            .andExpect(status().isOk)
            .andReturn()

        val content = result.response.contentAsString

        val idSaved = jdbi.withHandle<String, RuntimeException> { handle: Handle ->
            handle.createQuery("select id from report_evaluation")
                .mapTo(String::class.java).one()
        }
        assertEquals(idSaved, content)
    }

    @Test
    @Order(2)
    fun should_get_evaluations() {
        val request = MockMvcRequestBuilders.request(HttpMethod.GET, "/evaluations")
        val result = MockMvcBuilders.webAppContextSetup(wac).build().perform(request)
            .andExpect(status().isOk)
            .andReturn()

        val jsonArray = JSONArray(result.response.contentAsString)
        assertEquals(jsonArray.length(), 1)

        val evaluation = jsonArray.getJSONObject(0)

        assertEquals("质量评估", evaluation.getString("name"))
    }

    @Test
    @Order(3)
    fun should_get_evaluation_by_id() {
        val id = jdbi.withHandle<String, RuntimeException> { handle: Handle ->
            handle.createQuery("select id from report_evaluation")
                .mapTo(String::class.java).one()
        }
        val request = MockMvcRequestBuilders.request(HttpMethod.GET, "/evaluations/$id")
        val result = MockMvcBuilders.webAppContextSetup(wac).build().perform(request)
            .andExpect(status().isOk)
            .andReturn()

        val evaluation = JSONObject(result.response.contentAsString)

        assertEquals("质量评估", evaluation.getString("name"))
    }

    @Test
    @Order(4)
    fun should_get_evaluation_detail_by_id() {
        val id = jdbi.withHandle<String, RuntimeException> { handle: Handle ->
            handle.createQuery("select id from report_evaluation")
                .mapTo(String::class.java).one()
        }
        val request = MockMvcRequestBuilders.request(HttpMethod.GET, "/evaluation-details/$id")
        val result = MockMvcBuilders.webAppContextSetup(wac).build().perform(request)
            .andExpect(status().isOk)
            .andReturn()

        assertEquals(
            "{\"changeImpactReportDetail\":{\"scatteredCommits\":[],\"allCommits\":[],\"scatteredPercent\":\"NaN\"},\"codeStyleReportDetail\":null,\"dbCouplingReportDetail\":null,\"layerReportDetail\":null,\"moduleCouplingReportDetail\":{\"latestQualityList\":[],\"moduleInstabilityAverage\":\"NaN\",\"count8\":0,\"count8To6\":0,\"count6\":0},\"testProtectionReportDetail\":{\"testBs\":[],\"totalTest\":0,\"hotSpotTest\":[],\"hotSpotTestBadSmell\":[],\"classCoverageByFiles\":[],\"hotSpotFile\":[],\"classCoverageByModules\":[],\"hotSpotModule\":[],\"uselessTest\":0,\"latestUselessTest\":0,\"uselessPercent\":0.0,\"latestTestCoverage\":\"NaN\",\"latestModuleTestCoverage\":\"NaN\",\"testCoverage\":\"NaN\",\"modelCoverage\":\"NaN\"}}",
            result.response.contentAsString
        )
    }
}

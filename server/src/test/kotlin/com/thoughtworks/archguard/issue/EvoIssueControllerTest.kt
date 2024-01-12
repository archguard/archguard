package com.thoughtworks.archguard.issue;

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.`when`
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
class EvoIssueControllerTest {

    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var evoIssueService: EvoIssueService

    @BeforeEach
    fun setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(EvoIssueController(evoIssueService)).build()
    }

    @Test
    fun shouldReturnAllIssues() {
        val systemId = 1L
        val expectedIssues = listOf(
            EvoIssueModel("1", "position1", "name1", "detail1", "ruleType1", "severity1", "fullName1", "source1"),
            EvoIssueModel("2", "position2", "name2", "detail2", "ruleType2", "severity2", "fullName2", "source2")
        )

        `when`(evoIssueService.getAllIssues(systemId)).thenReturn(expectedIssues)

        mockMvc.perform(get("/api/systems/$systemId/issue"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].ruleId").value(1))
            .andExpect(jsonPath("$[0].position").value("position1"))
            .andExpect(jsonPath("$[0].name").value("name1"))
            .andExpect(jsonPath("$[0].detail").value("detail1"))
            .andExpect(jsonPath("$[0].ruleType").value("ruleType1"))
            .andExpect(jsonPath("$[0].severity").value("severity1"))
            .andExpect(jsonPath("$[0].fullName").value("fullName1"))
            .andExpect(jsonPath("$[0].source").value("source1"))
            .andExpect(jsonPath("$[1].ruleId").value(2))
            .andExpect(jsonPath("$[1].position").value("position2"))
            .andExpect(jsonPath("$[1].name").value("name2"))
            .andExpect(jsonPath("$[1].detail").value("detail2"))
            .andExpect(jsonPath("$[1].ruleType").value("ruleType2"))
            .andExpect(jsonPath("$[1].severity").value("severity2"))
            .andExpect(jsonPath("$[1].fullName").value("fullName2"))
            .andExpect(jsonPath("$[1].source").value("source2"))
    }
}

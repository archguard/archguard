package com.thoughtworks.archguard.report.controller

import com.thoughtworks.archguard.report.controller.coupling.FilterSizingDto
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.jdbc.SqlGroup
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.postForEntity

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
internal class CohesionControllerTest(@LocalServerPort val port: Int) {

    private val restTemplate = RestTemplate()

    @Test
    @SqlGroup(
        Sql("classpath:sqls/insert_commit_log_and_change_log.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        Sql("classpath:sqls/delete_commit_log_and_change_log.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    )
    fun should_get_shotgun() {
        val sizingDto = FilterSizingDto(1, 5, "", "", "", "")
        val entity = restTemplate.postForEntity(
            "http://localhost:$port/api/systems/0/cohesion/shotgun-surgery",
            sizingDto, ShotgunSurgeryListDto::class.java
        )

        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body?.count).isEqualTo(1)
        assertThat(entity.body?.currentPageNumber).isEqualTo(1)
        assertThat(entity.body?.data?.size).isEqualTo(1)
        assertThat(entity.body?.data?.get(0)?.commitMessage).isEqualTo("only leave java codes")
        assertThat(entity.body?.data?.get(0)?.clazzes?.size).isEqualTo(10)
    }
}

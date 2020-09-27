package com.thoughtworks.archguard.report.controller

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.jdbc.SqlGroup
import org.springframework.web.client.RestTemplate


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
internal class CohesionControllerTest(@LocalServerPort val port: Int) {

    private val restTemplate = RestTemplate()

    @Test
    @SqlGroup(
            Sql("classpath:sqls/insert_commit_log_and_change_log.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            Sql("classpath:sqls/delete_commit_log_and_change_log.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD))
    fun should_get_shotgun() {
        val entity = restTemplate.getForEntity("http://localhost:$port/systems/0/cohesion/shotgun-surgery?numberPerPage=1&currentPageNumber=1",
                ShotgunSurgeryListDto::class.java)
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body?.count).isEqualTo(1)
        assertThat(entity.body?.currentPageNumber).isEqualTo(1)
        assertThat(entity.body?.data?.size).isEqualTo(1)
        assertThat(entity.body?.data?.get(0)?.commitMessage).isEqualTo("only leave java codes")
        assertThat(entity.body?.data?.get(0)?.clazzes?.size).isEqualTo(9)
    }

}
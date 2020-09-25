package com.thoughtworks.archguard.report.controller

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.jdbc.Sql
import org.springframework.web.client.RestTemplate

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Disabled
internal class RedundancyControllerTest(@LocalServerPort val port: Int){
    private val restTemplate = RestTemplate()
    @Test
    @Sql("classpath:sqls/insert_redundancy_data.sql")
    fun should_get_rdundancy() {
        val entity = restTemplate.getForEntity("http://localhost:$port/systems/8/redundancy/class/one-method?numberPerPage=1&currentPageNumber=1",
                OneMethodClassDto::class.java)
        Assertions.assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        Assertions.assertThat(entity.body?.count).isEqualTo(1)
        Assertions.assertThat(entity.body?.currentPageNumber).isEqualTo(1)
        Assertions.assertThat(entity.body?.data?.size).isEqualTo(1)
    }
}
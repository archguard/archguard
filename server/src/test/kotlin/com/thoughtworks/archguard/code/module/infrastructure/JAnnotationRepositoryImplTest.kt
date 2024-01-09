package com.thoughtworks.archguard.code.module.infrastructure

import com.thoughtworks.archguard.code.module.domain.JAnnotationRepository
import org.archguard.model.code.JAnnotation
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.jdbc.Sql
import kotlin.test.assertEquals

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class JAnnotationRepositoryImplTest {
    @Autowired
    lateinit var jAnnotationRepository: JAnnotationRepository

    @Test
    @Sql("classpath:sqls/insert_jannotation.sql")
    internal fun should_get_JAnnotation_with_value_by_name() {
        val jAnnotations = jAnnotationRepository.getJAnnotationWithValueByName("feign.FeignClient")
        assertEquals(1, jAnnotations.size)
        val expectedJAnnotation = JAnnotation(
            "3b32c105-137c-4f1b-a064-a209250f729b",
            "TYPE",
            "a72fd1cd-6ca0-4585-b87c-d5e8b47e33ef",
            "org.springframework.cloud.netflix.feign.FeignClient"
        )
        expectedJAnnotation.values = mapOf("name" to "\"spring-cloud-producer\"")
        Assertions.assertThat(jAnnotations[0]).isEqualToComparingFieldByField(expectedJAnnotation)
    }
}

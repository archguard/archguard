package com.thoughtworks.archguard.module.domain.springcloud.feignclient

import com.thoughtworks.archguard.module.domain.JAnnotationRepository
import com.thoughtworks.archguard.module.domain.model.JAnnotation
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.lang.annotation.ElementType
import kotlin.test.assertEquals

class FeignClientServiceTest {
    @MockK
    lateinit var jAnnotationRepository: JAnnotationRepository

    private lateinit var service: FeignClientService


    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        service = FeignClientService(jAnnotationRepository)
    }

    @Test
    internal fun should_get_FeignClients() {

        // given
        val jAnnotation = JAnnotation("ida1", ElementType.TYPE.name, "id1", "org.springframework.cloud.netflix.feign.FeignClient")
        jAnnotation.values = mapOf("name" to "\"serviceName\"")

        every { jAnnotationRepository.getJAnnotationWithValueByName("feign.FeignClient") } returns listOf(jAnnotation)

        // when
        val feignClients = service.getFeignClients()

        // then
        assertEquals(1, feignClients.size)
        assertEquals("serviceName", feignClients[0].arg.name)
        assertEquals("id1", feignClients[0].targetId)
    }
}

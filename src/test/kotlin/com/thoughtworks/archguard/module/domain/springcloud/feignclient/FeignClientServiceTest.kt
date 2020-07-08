package com.thoughtworks.archguard.module.domain.springcloud.feignclient

import com.thoughtworks.archguard.module.domain.JAnnotationRepository
import com.thoughtworks.archguard.module.domain.JClassRepository
import com.thoughtworks.archguard.module.domain.model.JAnnotation
import com.thoughtworks.archguard.module.domain.model.JClass
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.lang.annotation.ElementType
import kotlin.test.assertEquals

class FeignClientServiceTest {
    @MockK
    lateinit var jAnnotationRepository: JAnnotationRepository

    @MockK
    lateinit var jClassRepository: JClassRepository

    @InjectMockKs
    var service: FeignClientService = FeignClientService()

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    internal fun should_get_FeignClients() {

        // given
        val jAnnotation = JAnnotation("ida1", ElementType.TYPE.name, "id1", "org.springframework.cloud.netflix.feign.FeignClient")
        jAnnotation.values = mapOf("name" to "\"serviceName\"")
        val jClass = JClass("id1", "class1", "module1")

        every { jAnnotationRepository.getJAnnotationWithValueByName("feign.FeignClient") } returns listOf(jAnnotation)
        every { jClassRepository.getJClassById("id1") } returns jClass

        // when
        val feignClients = service.getFeignClients()

        // then
        assertEquals(1, feignClients.size)
        assertEquals("serviceName", feignClients[0].arg.name)
        Assertions.assertThat(feignClients[0].jClass).isEqualTo(jClass)
    }
}

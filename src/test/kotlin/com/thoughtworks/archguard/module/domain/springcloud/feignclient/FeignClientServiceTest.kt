package com.thoughtworks.archguard.module.domain.springcloud.feignclient

import com.thoughtworks.archguard.clazz.domain.JClassRepository
import com.thoughtworks.archguard.module.domain.JAnnotationRepository
import com.thoughtworks.archguard.module.domain.model.Dependency
import com.thoughtworks.archguard.module.domain.model.JAnnotation
import com.thoughtworks.archguard.module.domain.springcloud.httprequest.HttpRequest
import com.thoughtworks.archguard.module.domain.springcloud.httprequest.HttpRequestArg
import com.thoughtworks.archguard.module.domain.springcloud.httprequest.HttpRequestService
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

    @MockK
    lateinit var httpRequestService: HttpRequestService

    @MockK
    lateinit var jClassRepository: JClassRepository

    @MockK
    lateinit var feignClientRepository: FeignClientRepository

    private lateinit var service: FeignClientService

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        service = FeignClientService(jAnnotationRepository, httpRequestService, jClassRepository, feignClientRepository)
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

    @Test
    internal fun should_get_feignClient_method_dependencies() {

        // given
        val feignClientAnnotation = JAnnotation("ida1", ElementType.TYPE.name, "idc1", "org.springframework.cloud.netflix.feign.FeignClient")
        feignClientAnnotation.values = mapOf("name" to "\"producer\"")

        val httpRequestMethod1 = HttpRequest("idm1", HttpRequestArg(listOf("/hello"), listOf("GET")))
        val httpRequestMethod2 = HttpRequest("idm2", HttpRequestArg(listOf("/hello"), listOf("GET")))

        every { jAnnotationRepository.getJAnnotationWithValueByName("feign.FeignClient") } returns listOf(feignClientAnnotation)
        every { httpRequestService.getHttpRequests() } returns listOf(httpRequestMethod1, httpRequestMethod2)
        every { jClassRepository.getMethodsById("idc1") } returns listOf("idm1")
        every { feignClientRepository.getServiceNameByMethodId("idm1") } returns "consumer"
        every { feignClientRepository.getServiceNameByMethodId("idm2") } returns "producer"


        // when
        val dependencies = service.getFeignClientMethodDependencies()

        // then
        assertEquals(1, dependencies.size)
        assertEquals("idm1", dependencies[0].caller.targetId)
        assertEquals("idm2", dependencies[0].callee.targetId)

    }


}

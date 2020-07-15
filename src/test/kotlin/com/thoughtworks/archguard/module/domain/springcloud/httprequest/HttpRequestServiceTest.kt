package com.thoughtworks.archguard.module.domain.springcloud.httprequest

import com.thoughtworks.archguard.module.domain.JAnnotationRepository
import com.thoughtworks.archguard.module.domain.model.JAnnotation
import com.thoughtworks.archguard.module.domain.springcloud.SpringCloudServiceRepository
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.lang.annotation.ElementType
import kotlin.test.assertEquals

class HttpRequestServiceTest {
    @MockK
    lateinit var jAnnotationRepository: JAnnotationRepository


    @MockK
    lateinit var springCloudServiceRepository: SpringCloudServiceRepository


    private lateinit var service: HttpRequestService


    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        service = HttpRequestService(jAnnotationRepository, springCloudServiceRepository)
        every { jAnnotationRepository.getJAnnotationWithValueByName(any()) } returns listOf()
    }

    @Test
    fun should_get_httpRequests() {

        // given
        val jAnnotation = JAnnotation("ida1", ElementType.METHOD.name, "id1", "org.springframework.web.bind.annotation.RequestMapping")
        jAnnotation.values = mapOf("value" to "[\"/hello/{name}\"]", "method" to "[[\"Lorg/springframework/web/bind/annotation/RequestMethod;\",\"POST\"]]")

        every { jAnnotationRepository.getJAnnotationWithValueByName("RequestMapping") } returns listOf(jAnnotation)

        // when
        val httpRequests = service.getHttpRequests()

        // then
        assertEquals(1, httpRequests.size)
        assertEquals("id1", httpRequests[0].targetId)
        assertEquals("/hello/{name}", httpRequests[0].arg.path[0])
        assertEquals("POST", httpRequests[0].arg.method[0])

    }

    @Test
    fun should_get_httpRequests_when_post_method() {

        // given
        val jAnnotation = JAnnotation("ida1", ElementType.METHOD.name, "id1", "org.springframework.web.bind.annotation.RequestMapping")
        jAnnotation.values = mapOf("value" to "[\"/hello/{name}\"]")

        every { jAnnotationRepository.getJAnnotationWithValueByName("PostMapping") } returns listOf(jAnnotation)


        // when
        val httpRequests = service.getHttpRequests()

        // then
        assertEquals(1, httpRequests.size)
        assertEquals("id1", httpRequests[0].targetId)
        assertEquals("/hello/{name}", httpRequests[0].arg.path[0])
        assertEquals("POST", httpRequests[0].arg.method[0])

    }

    @Test
    fun should_get_httpRequests_when_have_httpRequestClasses() {

        // given
        val jAnnotation1 = JAnnotation("ida1", ElementType.TYPE.name, "id1", "org.springframework.web.bind.annotation.PostMapping")
        jAnnotation1.values = mapOf("value" to "[\"/hello\"]")

        val jAnnotation2 = JAnnotation("ida2", ElementType.METHOD.name, "id2", "org.springframework.web.bind.annotation.PostMapping")
        jAnnotation2.values = mapOf("value" to "[\"/{name}\"]")


        every { jAnnotationRepository.getJAnnotationWithValueByName("RequestMapping") } returns listOf(jAnnotation1, jAnnotation2)
        every { springCloudServiceRepository.getMethodIdsByClassId("id1") } returns listOf("id2")

        // when
        val httpRequests = service.getHttpRequests()

        // then
        assertEquals(1, httpRequests.size)
        assertEquals("id2", httpRequests[0].targetId)
        assertEquals("/hello/{name}", httpRequests[0].arg.path[0])
        assertEquals("GET", httpRequests[0].arg.method[0])

    }}


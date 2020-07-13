package com.thoughtworks.archguard.module.domain.httprequest

import com.thoughtworks.archguard.module.domain.JAnnotationRepository
import com.thoughtworks.archguard.module.domain.model.JAnnotation
import com.thoughtworks.archguard.module.domain.springcloud.httprequest.HttpRequestService
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


    private lateinit var service: HttpRequestService


    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        service = HttpRequestService(jAnnotationRepository)
    }

    @Test
    internal fun should_get_httpRequests() {

        // given
        val jAnnotation = JAnnotation("ida1", ElementType.METHOD.name, "id1", "org.springframework.web.bind.annotation.RequestMapping")
        jAnnotation.values = mapOf("value" to "[\"/hello/{name}\"]", "method" to "[[\"Lorg/springframework/web/bind/annotation/RequestMethod;\",\"POST\"]]")

        every { jAnnotationRepository.getJAnnotationWithValueByName("RequestMapping") } returns listOf(jAnnotation)
        every { jAnnotationRepository.getJAnnotationWithValueByName("GetMapping") } returns listOf()
        every { jAnnotationRepository.getJAnnotationWithValueByName("PostMapping") } returns listOf()
        every { jAnnotationRepository.getJAnnotationWithValueByName("PutMapping") } returns listOf()
        every { jAnnotationRepository.getJAnnotationWithValueByName("DeleteMapping") } returns listOf()

        // when
        val httpRequests = service.getHttpRequests()

        // then
        assertEquals(1, httpRequests.size)
        assertEquals("id1", httpRequests[0].targetId)
        assertEquals("/hello/{name}", httpRequests[0].arg.path[0])
        assertEquals("POST", httpRequests[0].arg.method[0])

    }

    @Test
    internal fun should_get_httpRequests_when_post_method() {

        // given
        val jAnnotation = JAnnotation("ida1", ElementType.METHOD.name, "id1", "org.springframework.web.bind.annotation.PostMapping")
        jAnnotation.values = mapOf("value" to "[\"/hello/{name}\"]")

        every { jAnnotationRepository.getJAnnotationWithValueByName("RequestMapping") } returns listOf()
        every { jAnnotationRepository.getJAnnotationWithValueByName("GetMapping") } returns listOf()
        every { jAnnotationRepository.getJAnnotationWithValueByName("PostMapping") } returns listOf(jAnnotation)
        every { jAnnotationRepository.getJAnnotationWithValueByName("PutMapping") } returns listOf()
        every { jAnnotationRepository.getJAnnotationWithValueByName("DeleteMapping") } returns listOf()

        // when
        val httpRequests = service.getHttpRequests()

        // then
        assertEquals(1, httpRequests.size)
        assertEquals("id1", httpRequests[0].targetId)
        assertEquals("/hello/{name}", httpRequests[0].arg.path[0])
        assertEquals("POST", httpRequests[0].arg.method[0])

    }
}


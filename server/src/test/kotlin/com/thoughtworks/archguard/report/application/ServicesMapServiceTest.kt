package com.thoughtworks.archguard.report.application

import com.thoughtworks.archguard.report.domain.container.ContainerDemand
import com.thoughtworks.archguard.report.domain.container.ContainerResource
import com.thoughtworks.archguard.report.domain.container.ContainerServiceDO
import com.thoughtworks.archguard.report.domain.container.ContainerServiceRepo
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import kotlin.test.assertEquals

internal class ServicesMapServiceTest {
    @MockK
    lateinit var repo: ContainerServiceRepo
    private lateinit var service: ServicesMapService

    @BeforeEach
    internal fun setUp() {
        repo = mockk<ContainerServiceRepo>(relaxed = true)
        MockKAnnotations.init(this)
        service = ServicesMapService(repo)
    }

    @Test
    fun replaceDefaultParameterUrl() {
        val url = service.updateUrl("/api/systems/{systemId}/code-tree/")
        assertEquals("/api/systems/@uri@/code-tree/", url)
    }

    @Test
    fun replaceTwoParametersUrl() {
        val url = service.updateUrl("/api/systems/{systemId}/code-tree/{id}")
        assertEquals("/api/systems/@uri@/code-tree/@uri@", url)
    }

    @Test
    fun replaceKotlinLineStrRef() {
        val url = service.updateUrl("${'$'}baseUrl/api/systems/{systemId}/code-tree/{id}")
        assertEquals("@uri@/api/systems/@uri@/code-tree/@uri@", url)
    }


    @Test
    fun shouldReturnListOfContainerServiceResponseWhenFindAllServiceByIds() {
        // given
        val ids = listOf("1", "2")
        val systems = listOf(
            ContainerServiceDO(id = 1L, systemName = "1", language = "java"),
            ContainerServiceDO(id = 2L, systemName = "2", language = "kotlin")
        )
        val demands = listOf(ContainerDemand(
            systemId = "1",
            sourcePackage = "com.example",
            sourceClass = "ExampleClass",
            sourceMethod = "exampleMethod",
            targetUrl = "https://example.com/api",
            originUrl = "https://origin.com",
            targetHttpMethod = "POST"
        ))
        val resources = listOf(ContainerResource(
            systemId = "2",
            sourceUrl = "https://example.com/api",
            originUrl = "https://example.com",
            sourceHttpMethod = "GET",
            packageName = "com.example",
            className = "ApiService",
            methodName = "getData"
        ))

        every { repo.findSystems(ids) } returns systems
        every { repo.findDemandBySystemId(1L) } returns demands
        every { repo.findDemandBySystemId(2L) } returns demands
        every { repo.findResourceBySystemId(1L) } returns resources
        every { repo.findResourceBySystemId(2L) } returns resources

        // when
        val result = service.findAllServiceByIds(ids)

        // then
        assertEquals(2, result.size)
        assertEquals(systems[0].id, result[0].id)
        assertEquals(systems[0].language, result[0].language)
        assertEquals(systems[0].systemName, result[0].name)
        assertEquals(demands, result[0].demands)
        assertEquals(resources, result[0].resources)
        assertEquals(systems[1].id, result[1].id)
        assertEquals(systems[1].language, result[1].language)
        assertEquals(systems[1].systemName, result[1].name)
        assertEquals(demands, result[1].demands)
        assertEquals(resources, result[1].resources)
    }

    @Test
    fun shouldReturnContainerServiceResponseWhenFindBySystemId() {
        // given
        val systemId = 1L
        val demands = listOf(ContainerDemand(
            systemId = "1",
            sourcePackage = "com.example",
            sourceClass = "ExampleClass",
            sourceMethod = "exampleMethod",
            targetUrl = "https://example.com/api",
            originUrl = "https://origin.com",
            targetHttpMethod = "POST"
        ))
        val resources = listOf(
            ContainerResource(
                systemId = "1",
                sourceUrl = "https://example.com/api",
                originUrl = "https://example.com",
                sourceHttpMethod = "GET",
                packageName = "com.example",
                className = "ApiService",
                methodName = "getData"
            )
        )

        every { repo.findDemandBySystemId(systemId) } returns demands
        every { repo.findResourceBySystemId(systemId) } returns resources

        // when
        val result = service.findBySystemId(systemId)

        // then
        assertEquals(systemId, result.id)
        assertEquals(demands, result.demands)
        assertEquals(resources, result.resources)
    }
}

package org.archguard.scanner.analyser.backend

import chapi.domain.core.CodeDataStruct
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.archguard.context.ServiceSupplyType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import java.io.File


internal class JavaCompositeApiAnalyserTest {
    private fun loadNodes(source: String): List<CodeDataStruct> {
        return Json { ignoreUnknownKeys = true }.decodeFromString(
            File(this.javaClass.classLoader.getResource(source)!!.file).readText()
        )
    }

    @Test
    fun shouldCreateJavaHelloWorldApi() {
        val nodes = loadNodes("backend/structs_HelloController.json")
        val javaSpringAnalyser = JavaSpringAnalyser()
        nodes.forEach {
            javaSpringAnalyser.analysisByNode(it, "")
        }

        val services = javaSpringAnalyser.toContainerServices()
        val resources = services[0].resources
        assertEquals(1, resources.size)
        assertEquals("/", resources[0].sourceUrl)
        assertEquals("Get", resources[0].sourceHttpMethod)
        assertEquals("com.example.springboot", resources[0].packageName)
        assertEquals("HelloController", resources[0].className)
        assertEquals("index", resources[0].methodName)
    }

    @Test
    fun shouldHandleSubController() {
        val nodes = loadNodes("backend/structs_SubController.json")
        val javaSpringAnalyser = JavaSpringAnalyser()
        nodes.forEach {
            javaSpringAnalyser.analysisByNode(it, "")
        }

        val services = javaSpringAnalyser.toContainerServices()
        val resources = services[0].resources
        assertEquals(1, resources.size)
        assertEquals("/api/sub", resources[0].sourceUrl)
        assertEquals("Get", resources[0].sourceHttpMethod)
    }

    @Test
    fun shouldHandleErrorSlash() {
        val nodes = loadNodes("backend/structs_NormalController.json")
        val javaSpringAnalyser = JavaSpringAnalyser()
        nodes.forEach {
            javaSpringAnalyser.analysisByNode(it, "")
        }

        val services = javaSpringAnalyser.toContainerServices()
        val resources = services[0].resources
        assertEquals(1, resources.size)
        assertEquals("/api/sub/overview", resources[0].sourceUrl)
        assertEquals("Get", resources[0].sourceHttpMethod)
    }

    @Test
    fun url_method_in_annotation() {
        val nodes = loadNodes("backend/structs_DemoController.json")
        val javaSpringAnalyser = JavaSpringAnalyser()
        nodes.forEach {
            javaSpringAnalyser.analysisByNode(it, "")
        }

        val services = javaSpringAnalyser.toContainerServices()
        val resources = services[0].resources
        assertEquals(1, resources.size)
        assertEquals("/brand/listAll", resources[0].sourceUrl)
        assertEquals("Get", resources[0].sourceHttpMethod)
    }

    @Test
    fun identRestTemplateCall() {
        val nodes = loadNodes("backend/structs_kotlin.json")
        val apiAnalyser = JavaSpringAnalyser()
        nodes.forEach {
            apiAnalyser.analysisByNode(it, "")
        }

        val services = apiAnalyser.toContainerServices()
        assertEquals(1, services[0].demands.size)

        // Verify that URL template variables are resolved to path parameters
        val demand = services[0].demands[0]
        assertEquals("QualityGateClientImpl", demand.source_caller)
        assertEquals("Get", demand.target_http_method)
        // $baseUrl and $qualityGateName should be replaced with {baseUrl} and {qualityGateName}
        assertEquals("{baseUrl}/api/quality-gate-profile/{qualityGateName}", demand.target_url)
    }

    @Test
    fun identRestTemplateCallWithDifferentPatterns() {
        val nodes = loadNodes("backend/structs_RestTemplatePatterns.json")
        val apiAnalyser = JavaSpringAnalyser()
        nodes.forEach {
            apiAnalyser.analysisByNode(it, "")
        }

        val services = apiAnalyser.toContainerServices()
        val demands = services[0].demands

        // Should handle multiple RestTemplate calls with different HTTP methods
        assertEquals(4, demands.size)

        // Verify GET request with simple variable
        val getDemand = demands.find { it.target_http_method == "Get" }
        assertNotNull(getDemand)
        assertEquals("{apiHost}/users/{userId}", getDemand!!.target_url)

        // Verify POST request with brace variable ${varName}
        val postDemand = demands.find { it.target_http_method == "Post" }
        assertNotNull(postDemand)
        assertEquals("{apiHost}/users", postDemand!!.target_url)

        // Verify PUT request
        val putDemand = demands.find { it.target_http_method == "Put" }
        assertNotNull(putDemand)
        assertEquals("{apiHost}/users/{userId}", putDemand!!.target_url)

        // Verify DELETE request
        val deleteDemand = demands.find { it.target_http_method == "Delete" }
        assertNotNull(deleteDemand)
        assertEquals("{apiHost}/users/{userId}", deleteDemand!!.target_url)
    }

    @Test
    fun identDubboSupply() {
        val nodes = loadNodes("backend/structs_DubboService.json")
        val apiAnalyser = JavaDubboAnalyser()
        nodes.forEach {
            apiAnalyser.analysisByNode(it, "")
        }

        val services = apiAnalyser.toContainerServices()
        assertEquals(0, services[0].demands.size)
        assertEquals(1, services[0].resources.size)

        val resources = services[0].resources
        assertEquals("org.apache.dubbo.samples.annotation.api.GreetingService:greeting", resources[0].sourceUrl)
        assertEquals("rpc", resources[0].sourceHttpMethod)
        // supplyType
        assertEquals(ServiceSupplyType.DUBBO_API, resources[0].supplyType)
    }

    @Test
    fun identFeignClientDemands() {
        val nodes = loadNodes("backend/structs_FeignClient.json")
        val apiAnalyser = JavaFeignClientAnalyser()
        nodes.forEach {
            apiAnalyser.analysisByNode(it, "")
        }

        val services = apiAnalyser.toContainerServices()
        assertEquals(4, services[0].demands.size)
        assertEquals(0, services[0].resources.size)

        val demands = services[0].demands
        // Verify all HTTP methods are captured
        val methods = demands.map { it.target_http_method }.toSet()
        assertEquals(setOf("GET", "POST", "PUT", "DELETE"), methods)
    }

    @Test
    fun compositeAnalyserShouldIncludeFeignClient() {
        val nodes = loadNodes("backend/structs_FeignClient.json")
        val compositeAnalyser = JavaCompositeApiAnalyser()
        nodes.forEach {
            compositeAnalyser.analysisByNode(it, "")
        }

        val services = compositeAnalyser.toContainerServices()
        // The composite analyser returns separate services from each sub-analyser
        val allDemands = services.flatMap { it.demands }
        assertEquals(4, allDemands.size)
        
        // Verify FeignClient demands are present
        val feignDemands = allDemands.filter { it.base == "user-service" }
        assertEquals(4, feignDemands.size)
    }
}

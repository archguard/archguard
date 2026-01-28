package org.archguard.scanner.analyser.backend

import chapi.domain.core.CodeDataStruct
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.File

internal class JavaFeignClientAnalyserTest {
    
    private fun loadNodes(source: String): List<CodeDataStruct> {
        return Json { ignoreUnknownKeys = true }.decodeFromString(
            File(this.javaClass.classLoader.getResource(source)!!.file).readText()
        )
    }
    
    @Test
    fun `should identify FeignClient with multiple HTTP methods`() {
        val nodes = loadNodes("backend/structs_FeignClient.json")
        val analyser = JavaFeignClientAnalyser()
        
        nodes.forEach { analyser.analysisByNode(it, "") }
        
        val services = analyser.toContainerServices()
        assertEquals(1, services.size)
        
        val demands = services[0].demands
        assertEquals(4, demands.size)
        
        // Verify GET method
        val getRequest = demands.find { it.target_http_method == "GET" }
        assertNotNull(getRequest)
        assertEquals("user-service:/api/v1/users/{id}", getRequest!!.target_url)
        assertEquals("com.example.client.UserServiceClient.getUserById", getRequest.source_caller)
        assertEquals("user-service", getRequest.base)
        
        // Verify POST method
        val postRequest = demands.find { it.target_http_method == "POST" }
        assertNotNull(postRequest)
        assertEquals("user-service:/api/v1/users", postRequest!!.target_url)
        
        // Verify PUT method
        val putRequest = demands.find { it.target_http_method == "PUT" }
        assertNotNull(putRequest)
        assertEquals("user-service:/api/v1/users/{id}", putRequest!!.target_url)
        
        // Verify DELETE method
        val deleteRequest = demands.find { it.target_http_method == "DELETE" }
        assertNotNull(deleteRequest)
        assertEquals("user-service:/api/v1/users/{id}", deleteRequest!!.target_url)
    }
    
    @Test
    fun `should handle FeignClient with RequestMapping annotation`() {
        val nodes = loadNodes("backend/structs_FeignClientRequestMapping.json")
        val analyser = JavaFeignClientAnalyser()
        
        nodes.forEach { analyser.analysisByNode(it, "") }
        
        val services = analyser.toContainerServices()
        val demands = services[0].demands
        assertEquals(2, demands.size)
        
        // Verify GET method from RequestMapping
        val getRequest = demands.find { it.target_http_method == "GET" }
        assertNotNull(getRequest)
        assertEquals("order-service:/orders", getRequest!!.target_url)
        
        // Verify POST method from RequestMapping
        val postRequest = demands.find { it.target_http_method == "POST" }
        assertNotNull(postRequest)
        assertEquals("order-service:/orders", postRequest!!.target_url)
    }
    
    @Test
    fun `should handle FeignClient with url attribute`() {
        val nodes = loadNodes("backend/structs_FeignClientWithUrl.json")
        val analyser = JavaFeignClientAnalyser()
        
        nodes.forEach { analyser.analysisByNode(it, "") }
        
        val services = analyser.toContainerServices()
        val demands = services[0].demands
        assertEquals(1, demands.size)
        
        val request = demands[0]
        assertEquals("GET", request.target_http_method)
        assertEquals("http://api.external.com/data", request.target_url)
        assertEquals("external-api", request.base)
    }
    
    @Test
    fun `should handle legacy Netflix FeignClient annotation`() {
        val nodes = loadNodes("backend/structs_FeignClientLegacy.json")
        val analyser = JavaFeignClientAnalyser()
        
        nodes.forEach { analyser.analysisByNode(it, "") }
        
        val services = analyser.toContainerServices()
        val demands = services[0].demands
        assertEquals(1, demands.size)
        
        val request = demands[0]
        assertEquals("GET", request.target_http_method)
        assertEquals("legacy-service:/resources/{id}", request.target_url)
        assertEquals("legacy-service", request.base)
    }
    
    @Test
    fun `should ignore non-FeignClient classes`() {
        val nodes = loadNodes("backend/structs_RegularService.json")
        val analyser = JavaFeignClientAnalyser()
        
        nodes.forEach { analyser.analysisByNode(it, "") }
        
        val services = analyser.toContainerServices()
        val demands = services[0].demands
        assertEquals(0, demands.size)
    }
    
    @Test
    fun `should ignore FeignClient methods without HTTP annotations`() {
        val nodes = loadNodes("backend/structs_FeignClientNoMethodAnnotation.json")
        val analyser = JavaFeignClientAnalyser()
        
        nodes.forEach { analyser.analysisByNode(it, "") }
        
        val services = analyser.toContainerServices()
        val demands = services[0].demands
        assertEquals(0, demands.size)
    }
    
    @Test
    fun `should handle PatchMapping correctly`() {
        val nodes = loadNodes("backend/structs_FeignClientPatch.json")
        val analyser = JavaFeignClientAnalyser()
        
        nodes.forEach { analyser.analysisByNode(it, "") }
        
        val services = analyser.toContainerServices()
        val demands = services[0].demands
        assertEquals(1, demands.size)
        
        val request = demands[0]
        assertEquals("PATCH", request.target_http_method)
        assertEquals("patch-service:/resources/{id}", request.target_url)
    }
    
    @Test
    fun `should handle empty FeignClient annotation gracefully`() {
        val nodes = loadNodes("backend/structs_FeignClientEmpty.json")
        val analyser = JavaFeignClientAnalyser()
        
        nodes.forEach { analyser.analysisByNode(it, "") }
        
        val services = analyser.toContainerServices()
        val demands = services[0].demands
        // Should produce no demands when FeignClient has no name/value
        assertEquals(0, demands.size)
    }
    
    @Test
    fun `should correctly set call_routes`() {
        val nodes = loadNodes("backend/structs_FeignClient.json")
        val analyser = JavaFeignClientAnalyser()
        
        nodes.forEach { analyser.analysisByNode(it, "") }
        
        val services = analyser.toContainerServices()
        val demands = services[0].demands
        
        val getRequest = demands.find { it.target_http_method == "GET" }
        assertNotNull(getRequest)
        assertEquals(listOf("UserServiceClient", "getUserById"), getRequest!!.call_routes)
    }
    
    @Test
    fun `should return empty resources list`() {
        val nodes = loadNodes("backend/structs_FeignClient.json")
        val analyser = JavaFeignClientAnalyser()
        
        nodes.forEach { analyser.analysisByNode(it, "") }
        
        val services = analyser.toContainerServices()
        // FeignClient analyser should not produce resources, only demands
        assertEquals(0, services[0].resources.size)
    }
    
    @Test
    fun `should handle multiple FeignClient interfaces`() {
        // Load both FeignClient test files
        val nodes1 = loadNodes("backend/structs_FeignClient.json")
        val nodes2 = loadNodes("backend/structs_FeignClientRequestMapping.json")
        val analyser = JavaFeignClientAnalyser()
        
        nodes1.forEach { analyser.analysisByNode(it, "") }
        nodes2.forEach { analyser.analysisByNode(it, "") }
        
        val services = analyser.toContainerServices()
        val demands = services[0].demands
        
        // 4 from FeignClient.json + 2 from FeignClientRequestMapping.json
        assertEquals(6, demands.size)
        
        // Verify we have demands from both services
        val userServiceDemands = demands.filter { it.base == "user-service" }
        val orderServiceDemands = demands.filter { it.base == "order-service" }
        
        assertEquals(4, userServiceDemands.size)
        assertEquals(2, orderServiceDemands.size)
    }
    
    @Test
    fun `should handle path normalization correctly`() {
        val nodes = loadNodes("backend/structs_FeignClient.json")
        val analyser = JavaFeignClientAnalyser()
        
        nodes.forEach { analyser.analysisByNode(it, "") }
        
        val services = analyser.toContainerServices()
        val demands = services[0].demands
        
        // All paths should start with /
        demands.forEach { demand ->
            assertTrue(demand.target_url.contains(":/"))
        }
        
        // Paths should not have double slashes
        demands.forEach { demand ->
            assertFalse(demand.target_url.contains("//") && !demand.target_url.startsWith("http"))
        }
    }
}

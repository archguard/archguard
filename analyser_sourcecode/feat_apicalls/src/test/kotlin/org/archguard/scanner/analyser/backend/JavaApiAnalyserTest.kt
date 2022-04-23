package org.archguard.scanner.analyser.backend

import chapi.domain.core.CodeDataStruct
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.archguard.scanner.common.backend.JavaApiAnalyser
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.io.File


internal class JavaApiAnalyserTest {
    private fun loadNodes(source: String): List<CodeDataStruct> {
        return Json.decodeFromString(
            File(this.javaClass.classLoader.getResource(source)!!.file).readText()
        )
    }

    @Test
    fun shouldCreateJavaHelloWorldApi() {
        val nodes = loadNodes("backend/structs_HelloController.json")
        val javaApiAnalyser = JavaApiAnalyser()
        nodes.forEach {
            javaApiAnalyser.analysisByNode(it, "")
        }

        val services = javaApiAnalyser.toContainerServices()
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
        val javaApiAnalyser = JavaApiAnalyser()
        nodes.forEach {
            javaApiAnalyser.analysisByNode(it, "")
        }

        val services = javaApiAnalyser.toContainerServices()
        val resources = services[0].resources
        assertEquals(1, resources.size)
        assertEquals("/api/sub", resources[0].sourceUrl)
        assertEquals("Get", resources[0].sourceHttpMethod)
    }

    @Test
    fun shouldHandleErrorSlash() {
        val nodes = loadNodes("backend/structs_NormalController.json")
        val javaApiAnalyser = JavaApiAnalyser()
        nodes.forEach {
            javaApiAnalyser.analysisByNode(it, "")
        }

        val services = javaApiAnalyser.toContainerServices()
        val resources = services[0].resources
        assertEquals(1, resources.size)
        assertEquals("/api/sub/overview", resources[0].sourceUrl)
        assertEquals("Get", resources[0].sourceHttpMethod)
    }

    @Test
    fun url_method_in_annotation() {
        val nodes = loadNodes("backend/structs_DemoController.json")
        val javaApiAnalyser = JavaApiAnalyser()
        nodes.forEach {
            javaApiAnalyser.analysisByNode(it, "")
        }

        val services = javaApiAnalyser.toContainerServices()
        val resources = services[0].resources
        assertEquals(1, resources.size)
        assertEquals("/brand/listAll", resources[0].sourceUrl)
        assertEquals("Get", resources[0].sourceHttpMethod)
    }

    @Test
    fun identRestTemplateCall() {
        val nodes = loadNodes("backend/structs_kotlin.json")
        val apiAnalyser = JavaApiAnalyser()
        nodes.forEach {
            apiAnalyser.analysisByNode(it, "")
        }

        val services = apiAnalyser.toContainerServices()
        assertEquals(1, services[0].demands.size)
    }
}

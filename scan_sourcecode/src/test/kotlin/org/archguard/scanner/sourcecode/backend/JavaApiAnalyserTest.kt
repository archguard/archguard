package org.archguard.scanner.sourcecode.backend

import chapi.app.analyser.JavaAnalyserApp
import chapi.app.analyser.KotlinAnalyserApp
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Test
import kotlinx.serialization.encodeToString
import org.junit.jupiter.api.Assertions.*
import java.io.File
import java.nio.file.Paths

internal class JavaApiAnalyserTest {

    @Test
    fun shouldCreateJavaHelloWorldApi() {
        val resource = this.javaClass.classLoader.getResource("frameworks/spring/HelloController.java")!!
        val path = Paths.get(resource.toURI()).toFile().absolutePath

        val nodes = JavaAnalyserApp().analysisNodeByPath(path)
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
        val resource = this.javaClass.classLoader.getResource("frameworks/spring/SubController.java")!!
        val path = Paths.get(resource.toURI()).toFile().absolutePath

        val nodes = JavaAnalyserApp().analysisNodeByPath(path)
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
        val resource = this.javaClass.classLoader.getResource("frameworks/spring/NormalController.java")!!
        val path = Paths.get(resource.toURI()).toFile().absolutePath

        val nodes = JavaAnalyserApp().analysisNodeByPath(path)
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
    fun identRestTemplateCall() {
        val resource = this.javaClass.classLoader.getResource("resttemplate/")!!
        val path = Paths.get(resource.toURI()).toFile().absolutePath

        val nodes = JavaAnalyserApp().analysisNodeByPath(path)
        val javaApiAnalyser = JavaApiAnalyser()
        nodes.forEach {
            javaApiAnalyser.analysisByNode(it, "")
        }

        val services = javaApiAnalyser.toContainerServices()
        assertEquals(1, services[0].demands.size)
    }
}
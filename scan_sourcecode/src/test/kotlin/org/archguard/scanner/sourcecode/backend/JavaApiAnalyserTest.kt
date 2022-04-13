package org.archguard.scanner.sourcecode.backend

import chapi.app.analyser.JavaAnalyserApp
import chapi.app.analyser.KotlinAnalyserApp
import org.junit.jupiter.api.Test
import org.archguard.scanner.common.backend.JavaApiAnalyser
import org.junit.jupiter.api.Assertions.*
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
    fun url_method_in_annotation() {
        val resource = this.javaClass.classLoader.getResource("frameworks/spring/DemoController.java")!!
        val path = Paths.get(resource.toURI()).toFile().absolutePath

        val nodes = JavaAnalyserApp().analysisNodeByPath(path)
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
        val resource = this.javaClass.classLoader.getResource("kotlin/")!!
        val path = Paths.get(resource.toURI()).toFile().absolutePath

        val nodes = KotlinAnalyserApp().analysisNodeByPath(path)
        val apiAnalyser = JavaApiAnalyser()
        nodes.forEach {
            apiAnalyser.analysisByNode(it, "")
        }

        val services = apiAnalyser.toContainerServices()
        assertEquals(1, services[0].demands.size)
    }
}
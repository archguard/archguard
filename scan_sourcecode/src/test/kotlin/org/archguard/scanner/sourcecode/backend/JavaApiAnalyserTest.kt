package org.archguard.scanner.sourcecode.backend

import chapi.app.analyser.JavaAnalyserApp
import chapi.app.analyser.TypeScriptAnalyserApp
import org.junit.jupiter.api.Test

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
}
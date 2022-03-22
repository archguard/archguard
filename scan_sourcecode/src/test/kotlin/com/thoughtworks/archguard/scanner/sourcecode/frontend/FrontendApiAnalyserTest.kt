package com.thoughtworks.archguard.scanner.sourcecode.frontend

import chapi.app.analyser.TypeScriptAnalyserApp
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.io.File
import java.nio.file.Paths

internal class FrontendApiAnalyserTest {
    @Test
    fun shouldSupportIdentifyComponentApi() {
        val resource = this.javaClass.classLoader.getResource("languages/ts/apicall")!!
        val path = Paths.get(resource.toURI()).toFile().absolutePath

        val nodes = TypeScriptAnalyserApp().analysisNodeByPath(path)
        assertEquals(5, nodes.size)

        val componentCalls: Array<ContainerService> = FrontendApiAnalyser().analysis(nodes, path)
        File("api.json").writeText(Json.encodeToString(componentCalls))

        assertEquals(1, componentCalls.size)
        assertEquals("BadSmellThreshold/BadSmellThreshold", componentCalls[0].name)
        val apiRef = componentCalls[0].demands[0]
        assertEquals(naming("api/addition/systemInfo", "updateSystemInfo"), apiRef.source_caller)
        assertEquals("baseURL", apiRef.base)
        assertEquals("systemInfoApi", apiRef.target_url)
        assertEquals("PUT", apiRef.target_http_method)
        assertEquals("parameter", apiRef.call_data)
    }

    @Test
    internal fun shouldCorrectComponentName() {
        val resource = this.javaClass.classLoader.getResource("languages/ts/interface-error")!!
        val path = Paths.get(resource.toURI()).toFile().absolutePath

        val nodes = TypeScriptAnalyserApp().analysisNodeByPath(path)
        File("nodes.json").writeText(Json.encodeToString(nodes))

        val componentCalls: Array<ContainerService> = FrontendApiAnalyser().analysis(nodes, path)
        File("api.json").writeText(Json.encodeToString(componentCalls))
    }

    @Test
    internal fun shouldSaveApiAdapter() {
        val resource = this.javaClass.classLoader.getResource("languages/ts/api-adapter")!!
        val path = Paths.get(resource.toURI()).toFile().absolutePath

        val nodes = TypeScriptAnalyserApp().analysisNodeByPath(path)

        val componentCalls: Array<ContainerService> = FrontendApiAnalyser().analysis(nodes, path)
        assertEquals(4, componentCalls[0].demands.size)
    }

    @Test
    internal fun testForUmi() {
        val resource = this.javaClass.classLoader.getResource("languages/ts/js-umi-request")!!
        val path = Paths.get(resource.toURI()).toFile().absolutePath

        val nodes = TypeScriptAnalyserApp().analysisNodeByPath(path)
        assertEquals(2, nodes.size)
        File("nodes.json").writeText(Json.encodeToString(nodes))

        val componentCalls: Array<ContainerService> = FrontendApiAnalyser().analysis(nodes, path)
        val apiRef = componentCalls[0].demands

        assertEquals(1, apiRef.size)
        assertEquals(naming("system-info", "querySystemInfo"), apiRef[0].source_caller)
        assertEquals("GET", apiRef[0].target_http_method)
        File("api.json").writeText(Json.encodeToString(componentCalls))
    }
}

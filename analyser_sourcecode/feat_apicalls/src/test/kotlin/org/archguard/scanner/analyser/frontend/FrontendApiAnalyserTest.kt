package org.archguard.scanner.analyser.frontend

import chapi.domain.core.CodeDataStruct
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.archguard.scanner.core.sourcecode.ContainerService
import org.archguard.scanner.sourcecode.frontend.FrontendApiAnalyser
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.io.File

internal class FrontendApiAnalyserTest {

    private fun loadNodes(source: String): Pair<String, Array<CodeDataStruct>> {
        val file = File(this.javaClass.classLoader.getResource(source)!!.file)
        return file.absolutePath to Json.decodeFromString(file.readText())
    }

    @Test
    fun shouldSupportIdentifyComponentApi() {
        val (path, nodes) = loadNodes("frontend/structs_apicall.json")

        val componentCalls: Array<ContainerService> = FrontendApiAnalyser().analysisByPath(nodes, path)
        File("api.json").writeText(Json.encodeToString(componentCalls))

        assertEquals(1, componentCalls.size)
        assertEquals("src/BadSmellThreshold/BadSmellThreshold", componentCalls[0].name)
        val apiRef = componentCalls[0].demands[0]
        assertEquals(naming("src/api/addition/systemInfo", "updateSystemInfo"), apiRef.source_caller)
        assertEquals("src/api/addition/systemInfo::updateSystemInfo", apiRef.call_routes.joinToString(","))
        assertEquals("baseURL", apiRef.base)
        assertEquals("systemInfoApi", apiRef.target_url)
        assertEquals("PUT", apiRef.target_http_method)
        assertEquals("parameter", apiRef.call_data)
    }

    @Test
    internal fun shouldCorrectComponentName() {
        val (path, nodes) = loadNodes("frontend/structs_interface-error.json")

        val componentCalls: Array<ContainerService> = FrontendApiAnalyser().analysisByPath(nodes, path)
        File("api.json").writeText(Json.encodeToString(componentCalls))
    }

    @Test
    internal fun shouldSaveApiAdapter() {
        val (path, nodes) = loadNodes("frontend/structs_api-adapter.json")

        val componentCalls: Array<ContainerService> = FrontendApiAnalyser().analysisByPath(nodes, path)
        assertEquals(4, componentCalls[0].demands.size)
    }

    @Test
    internal fun testForUmi() {
        val (path, nodes) = loadNodes("frontend/structs_js-umi-request.json")

        val componentCalls: Array<ContainerService> = FrontendApiAnalyser().analysisByPath(nodes, path)
        val apiRef = componentCalls[0].demands

        assertEquals(1, apiRef.size)
        assertEquals(naming("src/system-info", "querySystemInfo"), apiRef[0].source_caller)
        assertEquals("src/system-info::querySystemInfo", apiRef[0].call_routes.joinToString(","))
        assertEquals("GET", apiRef[0].target_http_method)
        File("api.json").writeText(Json.encodeToString(componentCalls))
    }
}

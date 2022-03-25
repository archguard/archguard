package org.archguard.scanner.sourcecode

import chapi.app.analyser.TypeScriptAnalyserApp
import chapi.domain.core.CodeDataStruct
import org.archguard.scanner.sourcecode.frontend.ContainerService
import org.archguard.scanner.sourcecode.frontend.FrontendApiAnalyser
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.io.File
import java.nio.file.Paths
import java.util.HashMap
import kotlin.test.assertEquals

internal class ContainerRepositoryTest {
    @Test
    internal fun container_convert() {
        val resource = this.javaClass.classLoader.getResource("languages/ts/apicall")!!
        val path = Paths.get(resource.toURI()).toFile().absolutePath

        val ds = TypeScriptAnalyserApp().analysisNodeByPath(path)

        val clzRepo = ContainerRepository("1", "typescript", "archguard")

        val feApiAnalyser = FrontendApiAnalyser()
        ds.forEach {
            feApiAnalyser.analysisByNode(it, path)
        }

        val apiCalls = feApiAnalyser.toContainerServices()

        clzRepo.saveContainerServices(apiCalls)
        clzRepo.close()
        assertEquals(1, apiCalls.size)
    }
}
package org.archguard.scanner.sourcecode.database

import chapi.app.analyser.KotlinAnalyserApp
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Test
import java.io.File
import java.nio.file.Paths

internal class DatabaseAnalyserTest {
    @Test
    fun shouldSupportIdentifyComponentApi() {
        val resource = this.javaClass.classLoader.getResource("jdbi/PackageRepositoryImpl.kt")!!
        val path = Paths.get(resource.toURI()).toFile().absolutePath

        val nodes = KotlinAnalyserApp().analysisNodeByPath(path)
        File("nodes.json").writeText(Json.encodeToString(nodes))
    }
}
package com.thoughtworks.archguard.smartscanner

import com.thoughtworks.archguard.scanner.infrastructure.command.StreamConsumer
import org.archguard.scanner.core.context.AnalyserType
import java.io.File

data class ScannerCommand(
    val type: AnalyserType,
    val systemId: String,
    val serverUrl: String,
    val path: String,
    // for archguard usage
    val workspace: File,
    val logStream: StreamConsumer,
) {
    fun toArguments(): List<String> {
        val arguments = mutableListOf(
            "--type=${type.name.lowercase()}",
            "--system-id=$systemId",
            "--server-url=$serverUrl",
            "--path=${workspace.resolve(path).absolutePath}",
            "--workspace=${workspace.absolutePath}",
        )
        // additional args
        language?.let { arguments.add("--language=$it") }
        features.forEach { arguments.add("--features=$it") }
        outputs.forEach { arguments.add("--output=$it") }
        return arguments
    }

    // for source code analysing, may be Map, String or AnalyserSpec
    // TODO support official analyser only, accept config and json to enable customized analyser
    var language: Any? = null
    var features: List<Any> = listOf()

    // TODO configurable output format
    var outputs = listOf("http")
}

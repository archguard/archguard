package com.thoughtworks.archguard.smartscanner

import com.thoughtworks.archguard.scanner.infrastructure.command.StreamConsumer
import org.archguard.scanner.core.context.AnalyserType
import java.io.File

data class ScannerCommand(
    val type: AnalyserType,
    val systemId: String,
    val serverUrl: String,
    // for frontend to resolve relative path usage
    val path: String,
    val workspace: File,
    val logStream: StreamConsumer,
    val branch: String,
) {
    fun toArguments(): List<String> {
        var workdir = workspace.resolve(path).absolutePath
        if(this.type == AnalyserType.GIT) {
            workdir = workspace.absolutePath
        }

        val arguments = mutableListOf(
            "--type=${type.name.lowercase()}",
            "--system-id=$systemId",
            "--server-url=$serverUrl",
            // different for git by workspace
            "--path=${workdir}",
            "--workspace=${workspace.absolutePath}",
            "--branch=$branch",
        )
        // additional args
        outputs.forEach { arguments.add("--output=$it") }
        language?.let { arguments.add("--language=$it") }
        features.forEach { arguments.add("--features=$it") }
        repoId?.let { arguments.add("--repo-id=$it") }
        rules.forEach { arguments.add("--rules=$it") }
        additionArguments.forEach(arguments::add)
        return arguments
    }

    // TODO configurable output format
    var outputs = listOf("http")

    // for source code analysing, may be Map, String or AnalyserSpec
    // TODO support official analyser only, accept config and json to enable customized analyser
    var language: String? = null
    var features: List<String> = listOf()
    var rules: List<String> = listOf()
    var repoId: String? = null
    var additionArguments: List<String> = emptyList()
}

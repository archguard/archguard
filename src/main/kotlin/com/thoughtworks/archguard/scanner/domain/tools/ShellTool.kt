package com.thoughtworks.archguard.scanner.domain.tools

import com.thoughtworks.archguard.scanner.infrastructure.Processor
import java.io.File

class ShellTool(private val workspace: File) {

    fun countTest(): File {
        val outputFile = File("${workspace.absolutePath}/countTest.log")
        call(listOf("/bin/sh", "-c", "find . -name \"*.*\"" +
                "| grep -E \"(src/test/kotlin|src/test/java)\" " +
                "| xargs grep \"@Test\" " +
                "| wc -l > ${outputFile.absolutePath}"))
        return outputFile
    }

    private fun call(cmd: List<String>) {
        Processor.executeWithLogs(ProcessBuilder(cmd), workspace)
    }
}
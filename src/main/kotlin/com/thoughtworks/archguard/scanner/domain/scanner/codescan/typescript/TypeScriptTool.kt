package com.thoughtworks.archguard.scanner.domain.scanner.codescan.typescript

import com.thoughtworks.archguard.scanner.infrastructure.FileOperator
import com.thoughtworks.archguard.scanner.infrastructure.Processor
import org.slf4j.LoggerFactory
import java.io.File
import java.net.URL

class TypeScriptTool(val systemRoot: File, val systemId: Long) {
    private val log = LoggerFactory.getLogger(TypeScriptTool::class.java)
    private val host = "https://github.com/archguard/scanner/releases/download/v1.1.6"
    private val version = "1.1.6"
    private val SCAN_TYPESCRIPT_JAR = "scan_typescript-$version-all.jar"

    fun analyse() {
        prepareTool()
        scan(listOf("java", "-jar", "scan_typescript.jar", "--path=.", "--system-id=$systemId"))
    }

    private fun prepareTool() {
        val jarExist = checkIfExistInLocal()
        if (jarExist) {
            copyIntoSystemRoot()
        } else {
            download()
        }
    }

    private fun copyIntoSystemRoot() {
        log.info("copy TypeScript jar tool from local")
        FileOperator.copyTo(File(SCAN_TYPESCRIPT_JAR), File("$systemRoot/scan_typescript.jar"))
        val chmod = ProcessBuilder("chmod", "+x", "scan_typescript.jar")
        chmod.directory(systemRoot)
        chmod.start().waitFor()
    }

    private fun checkIfExistInLocal(): Boolean {
        return File(SCAN_TYPESCRIPT_JAR).exists()
    }

    private fun download() {
        log.info("download TypeScript tool")
        val downloadUrl = "$host/$SCAN_TYPESCRIPT_JAR"
        FileOperator.download(URL(downloadUrl), File("$systemRoot/scan_typescript.jar"))
        val chmod = ProcessBuilder("chmod", "+x", "scan_typescript.jar")
        chmod.directory(systemRoot)
        chmod.start().waitFor()
    }

    private fun scan(cmd: List<String>) {
        Processor.executeWithLogs(ProcessBuilder(cmd), systemRoot)
    }

}

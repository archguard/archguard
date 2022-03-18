package com.thoughtworks.archguard.scanner.domain.scanner.codescan.sourcecode

import com.thoughtworks.archguard.scanner.infrastructure.FileOperator
import com.thoughtworks.archguard.scanner.infrastructure.Processor
import org.slf4j.LoggerFactory
import java.io.File
import java.net.URL

class SourceCodeTool(val systemRoot: File, val systemId: Long, val language: String) {
    private val log = LoggerFactory.getLogger(SourceCodeTool::class.java)
    private val host = "https://github.com/archguard/scanner/releases/download/v1.1.7"
    private val version = "1.1.7"
    private val SCAN_SOURCECODE_JAR = "scan_sourcecode-$version-all.jar"

    fun analyse() {
        prepareTool()
        val language =
        scan(listOf("java", "-jar", "scan_sourcecode.jar", "--path=.", "--system-id=$systemId", "--language=$language"))
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
        FileOperator.copyTo(File(SCAN_SOURCECODE_JAR), File("$systemRoot/scan_sourcecode.jar"))
        val chmod = ProcessBuilder("chmod", "+x", "scan_sourcecode.jar")
        chmod.directory(systemRoot)
        chmod.start().waitFor()
    }

    private fun checkIfExistInLocal(): Boolean {
        return File(SCAN_SOURCECODE_JAR).exists()
    }

    private fun download() {
        log.info("download TypeScript tool")
        val downloadUrl = "$host/$SCAN_SOURCECODE_JAR"
        FileOperator.download(URL(downloadUrl), File("$systemRoot/scan_sourcecode.jar"))
        val chmod = ProcessBuilder("chmod", "+x", "scan_sourcecode.jar")
        chmod.directory(systemRoot)
        chmod.start().waitFor()
    }

    private fun scan(cmd: List<String>) {
        Processor.executeWithLogs(ProcessBuilder(cmd), systemRoot)
    }

}

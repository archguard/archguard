package com.thoughtworks.archguard.scanner.domain.tools

import com.thoughtworks.archguard.scanner.infrastructure.FileOperator
import com.thoughtworks.archguard.scanner.infrastructure.Processor
import org.slf4j.LoggerFactory
import java.io.File
import java.net.URL

class JavaByteCodeTool(val systemRoot: File, val dbUrl: String, val systemId: Long) {

    private val log = LoggerFactory.getLogger(JavaByteCodeTool::class.java)
    private val host = "https://github.com/archguard/scanner/releases/download/v1.1.3"
    private val version = "1.1.3"
    private val SCAN_JAVA_BYTECODE_JAR = "scan_java_bytecode-$version-all.jar"

    fun analyse() {
        prepareTool()
        scan(listOf("java", "-jar", "-Ddburl=$dbUrl?useSSL=false", "scan_java_bytecode.jar", "-i", ".", "-xml", "false", "-id", "$systemId"))
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
        log.info("copy jar tool from local")
        FileOperator.copyTo(File(SCAN_JAVA_BYTECODE_JAR), File(systemRoot.toString() + "/scan_java_bytecode.jar"))
        val chmod = ProcessBuilder("chmod", "+x", "scan_java_bytecode.jar")
        chmod.directory(systemRoot)
        chmod.start().waitFor()
    }

    private fun checkIfExistInLocal(): Boolean {
        return File(SCAN_JAVA_BYTECODE_JAR).exists()
    }

    private fun download() {
        log.info("download jar tool")
        val downloadUrl = "$host/$SCAN_JAVA_BYTECODE_JAR"
        FileOperator.download(URL(downloadUrl), File("$systemRoot/scan_java_bytecode.jar"))
        val chmod = ProcessBuilder("chmod", "+x", "scan_java_bytecode.jar")
        chmod.directory(systemRoot)
        chmod.start().waitFor()
    }

    private fun scan(cmd: List<String>) {
        Processor.executeWithLogs(ProcessBuilder(cmd), systemRoot)
    }

}

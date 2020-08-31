package com.thoughtworks.archgard.scanner.domain.tools

import com.thoughtworks.archgard.scanner.infrastructure.FileOperator
import com.thoughtworks.archgard.scanner.infrastructure.Processor
import org.slf4j.LoggerFactory
import java.io.File
import java.net.URL

class JavaByteCodeTool(val systemRoot: File, val dbUrl: String, val systemId: Long) {
    private val log = LoggerFactory.getLogger(JavaByteCodeTool::class.java)
    private val host = "ec2-68-79-38-105.cn-northwest-1.compute.amazonaws.com.cn:8080"

    fun analyse() {
        prepareTool()
        scan(listOf("java", "-jar", "-Ddburl=" + dbUrl + "?useSSL=false", "scan_java_bytecode.jar", "-i", ".", "-id", "$systemId"))
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
        FileOperator.copyTo(File("scan_java_bytecode-1.3-jar-with-dependencies.jar"), File(systemRoot.toString() + "/scan_java_bytecode.jar"))
        val chmod = ProcessBuilder("chmod", "+x", "scan_java_bytecode.jar")
        chmod.directory(systemRoot)
        chmod.start().waitFor()
    }

    private fun checkIfExistInLocal(): Boolean {
        return File("scan_java_bytecode-1.3-jar-with-dependencies.jar").exists()
    }

    private fun download() {
        log.info("download jar tool")
        val downloadUrl = "http://$host/job/code-scanners/lastSuccessfulBuild/artifact/scan_java_bytecode/target/scan_java_bytecode-1.3-jar-with-dependencies.jar"
        FileOperator.download(URL(downloadUrl), File(systemRoot.toString() + "/scan_java_bytecode.jar"))
        val chmod = ProcessBuilder("chmod", "+x", "scan_java_bytecode.jar")
        chmod.directory(systemRoot)
        chmod.start().waitFor()
    }

    private fun scan(cmd: List<String>) {
        Processor.executeWithLogs(ProcessBuilder(cmd), systemRoot)
    }

}

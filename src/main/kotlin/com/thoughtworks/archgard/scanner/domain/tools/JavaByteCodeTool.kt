package com.thoughtworks.archgard.scanner.domain.tools

import com.thoughtworks.archgard.scanner.infrastructure.FileOperator
import com.thoughtworks.archgard.scanner.infrastructure.Processor
import org.slf4j.LoggerFactory
import java.io.File
import java.net.URL

class JavaByteCodeTool(val projectRoot: File, val dbUrl: String) {
    private val log = LoggerFactory.getLogger(JavaByteCodeTool::class.java)

    fun analyse() {
        prepareTool()
        scan(listOf("java", "-jar", "-Ddburl=" + dbUrl + "?useSSL=false", "scan_java_bytecode.jar", "."))
    }

    private fun prepareTool() {
        val jarExist = checkIfExistInLocal()
        if (jarExist) {
            copyIntoProjectRoot()
        } else {
            download()
        }
    }

    private fun copyIntoProjectRoot() {
        log.info("copy jar tool from local")
        FileOperator.copyTo(File("scan_java_bytecode-1.2-jar-with-dependencies.jar"), File(projectRoot.toString() + "/scan_java_bytecode.jar"))
        val chmod = ProcessBuilder("chmod", "+x", "scan_java_bytecode.jar")
        chmod.directory(projectRoot)
        chmod.start().waitFor()
    }

    private fun checkIfExistInLocal(): Boolean {
        return File("scan_java_bytecode-1.2-jar-with-dependencies.jar").exists()
    }

    private fun download() {
        log.info("download jar tool")
        val downloadUrl = "http://ci.archguard.org/job/code-scanners/lastSuccessfulBuild/artifact/scan_java_bytecode/target/scan_java_bytecode-1.2-jar-with-dependencies.jar"
        FileOperator.download(URL(downloadUrl), File(projectRoot.toString() + "/scan_java_bytecode.jar"))
        val chmod = ProcessBuilder("chmod", "+x", "scan_java_bytecode.jar")
        chmod.directory(projectRoot)
        chmod.start().waitFor()
    }

    private fun scan(cmd: List<String>) {
        Processor.executeWithLogs(ProcessBuilder(cmd), projectRoot)
    }

}
package com.thoughtworks.archguard.scanner.domain.tools

import com.thoughtworks.archguard.scanner.domain.system.BuildTool
import com.thoughtworks.archguard.scanner.infrastructure.FileOperator
import com.thoughtworks.archguard.scanner.infrastructure.Processor
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import java.io.File
import java.net.URL

class JacocoTool(val workspace: File, val systemRoot: File, val buildTool: BuildTool) {

    private val log = LoggerFactory.getLogger(JacocoTool::class.java)
    private val host = "https://github.com/archguard/scanner/releases/download/v1.1.3"
    private val version = "1.1.3"
    private val SCAN_JACOCO_JAR = "scan_jacoco-$version-all.jar"

    fun execToSql(): File? {
        prepareTool()
        call(listOf("java", "-jar", "scan_jacoco.jar", "--target-project=${systemRoot.absolutePath}",
                "--bin=${buildTool.target}/classes",
                "--exec=${buildTool.target}/jacoco.exec"))
        val sqlFile = File("${workspace.absolutePath}/jacoco.sql")
        return if (sqlFile.exists()) {
            sqlFile
        } else {
            log.info("failed to get jacoco.sql")
            null
        }
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
        FileOperator.copyTo(File(SCAN_JACOCO_JAR), File(workspace.absolutePath.toString() + "/scan_jacoco.jar"))
        val chmod = ProcessBuilder("chmod", "+x", "scan_jacoco.jar")
        chmod.directory(workspace)
        chmod.start().waitFor()
    }

    private fun checkIfExistInLocal(): Boolean {
        return File("scan_jacoco-$version-all.jar").exists()
    }

    private fun download() {
        if (File(workspace.absolutePath + "/scan_jacoco.jar").exists()) {
            return
        }
        val jarLink = "$host/$SCAN_JACOCO_JAR"
        FileOperator.download(URL(jarLink), File(workspace.absolutePath + "/scan_jacoco.jar"))
        val chmod = ProcessBuilder("chmod", "+x", "scan_jacoco.jar")
        chmod.directory(workspace)
        chmod.start().waitFor()
    }

    private fun call(cmd: List<String>) {
        Processor.executeWithLogs(ProcessBuilder(cmd), workspace)
    }
}

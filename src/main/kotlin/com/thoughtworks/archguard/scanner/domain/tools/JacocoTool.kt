package com.thoughtworks.archguard.scanner.domain.tools

import com.thoughtworks.archguard.scanner.domain.system.BuildTool
import com.thoughtworks.archguard.scanner.infrastructure.FileOperator
import com.thoughtworks.archguard.scanner.infrastructure.Processor
import org.slf4j.LoggerFactory
import java.io.File
import java.net.URL

class JacocoTool(val workspace: File, val systemRoot: File, val buildTool: BuildTool) {

    private val log = LoggerFactory.getLogger(JacocoTool::class.java)
    private val host = "ec2-68-79-38-105.cn-northwest-1.compute.amazonaws.com.cn:8080"

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
        FileOperator.copyTo(File("scan_jacoco-1.0-SNAPSHOT-jar-with-dependencies.jar"), File(workspace.absolutePath.toString() + "/scan_jacoco.jar"))
        val chmod = ProcessBuilder("chmod", "+x", "scan_jacoco.jar")
        chmod.directory(workspace)
        chmod.start().waitFor()
    }

    private fun checkIfExistInLocal(): Boolean {
        return File("scan_jacoco-1.0-SNAPSHOT-jar-with-dependencies.jar").exists()
    }

    private fun download() {
        if (File(workspace.absolutePath + "/scan_jacoco.jar").exists()) {
            return
        }
        val jarLink = "http://$host/job/code-scanners/lastSuccessfulBuild/artifact/scan_jacoco/target/scan_jacoco-1.0-SNAPSHOT-jar-with-dependencies.jar"
        FileOperator.download(URL(jarLink), File(workspace.absolutePath + "/scan_jacoco.jar"))
        val chmod = ProcessBuilder("chmod", "+x", "scan_jacoco.jar")
        chmod.directory(workspace)
        chmod.start().waitFor()
    }

    private fun call(cmd: List<String>) {
        Processor.executeWithLogs(ProcessBuilder(cmd), workspace)
    }
}
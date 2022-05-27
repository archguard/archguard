package com.thoughtworks.archguard.scanner.domain.scanner.javaext.jacoco

import com.thoughtworks.archguard.scanner.domain.system.BuildTool
import com.thoughtworks.archguard.scanner.infrastructure.FileOperator
import com.thoughtworks.archguard.scanner.infrastructure.command.Processor
import com.thoughtworks.archguard.scanner.infrastructure.command.StreamConsumer
import org.slf4j.LoggerFactory
import java.io.File
import java.net.URL

class JacocoTool(
    val workspace: File,
    val systemRoot: File,
    val buildTool: BuildTool,
    val logStream: StreamConsumer,
    val scannerVersion: String,
) {
    private val log = LoggerFactory.getLogger(JacocoTool::class.java)
    private val host = "https://github.com/archguard/scanner/releases/download/v$scannerVersion"
    private val SCAN_JACOCO_JAR = "scan_jacoco-$scannerVersion-all.jar"

    fun execToSql(): File? {
        prepareTool()
        call(
            listOf(
                "java", "-jar", "scan_jacoco.jar", "--target-project=${systemRoot.absolutePath}",
                "--bin=${buildTool.target}/classes",
                "--exec=${buildTool.target}/jacoco.exec"
            )
        )
        val sqlFile = File("$workspace/jacoco.sql")
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
        FileOperator.copyTo(File(SCAN_JACOCO_JAR), File("$workspace/scan_jacoco.jar"))
        try {
            val chmod = ProcessBuilder("chmod", "+x", "scan_jacoco.jar")
            chmod.directory(workspace)
            chmod.start().waitFor()
        } catch (ex: Exception) {
            log.warn("chmod +x scan_jacoco.jar tool Exception")
        }
    }

    private fun checkIfExistInLocal(): Boolean {
        return File(SCAN_JACOCO_JAR).exists()
    }

    private fun download() {
        if (File(SCAN_JACOCO_JAR).exists()) {
            return
        }
        val jarLink = "$host/$SCAN_JACOCO_JAR"

        log.info("start download scan_jacoco tool")
        FileOperator.download(URL(jarLink), File(SCAN_JACOCO_JAR))
        log.info("downloaded scan_jacoco tool")
        copyIntoSystemRoot()
    }

    private fun call(cmd: List<String>) {
        Processor.executeWithLogs(ProcessBuilder(cmd), workspace, logStream)
    }
}

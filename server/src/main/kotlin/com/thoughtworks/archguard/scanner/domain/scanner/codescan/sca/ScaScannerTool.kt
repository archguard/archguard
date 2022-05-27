package com.thoughtworks.archguard.scanner.domain.scanner.codescan.sca

import com.thoughtworks.archguard.scanner.infrastructure.FileOperator
import com.thoughtworks.archguard.scanner.infrastructure.command.Processor
import com.thoughtworks.archguard.scanner.infrastructure.command.StreamConsumer
import org.slf4j.LoggerFactory
import java.io.File
import java.net.URL

class ScaScannerTool(
    val systemRoot: File,
    val systemId: Long,
    val language: String,
    val logStream: StreamConsumer,
    val scannerVersion: String,
) {

    private val log = LoggerFactory.getLogger(ScaScannerTool::class.java)
    private val host = "https://github.com/archguard/archguard/releases/download/v$scannerVersion"
    private val SCA_JAR = "analyser_sca-$scannerVersion-all.jar"

    fun getScaReport(): File? {
        prepareTool()
        scan(
            listOf(
                "java", "-jar", "analyser_sca.jar", "--path=.", "--system-id=$systemId", "--language=$language"
            )
        )
        val report = File("$systemRoot/output.sql")
        return if (report.exists()) {
            report
        } else {
            log.info("failed to get output.sql")
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
        FileOperator.copyTo(File(SCA_JAR), File("$systemRoot/analyser_sca.jar"))
        try {
            val chmod = ProcessBuilder("chmod", "+x", "analyser_sca.jar")
            chmod.directory(systemRoot)
            chmod.start().waitFor()
        } catch (ex: Exception) {
            log.warn("chmod +x scan_git.jar tool Exception")
        }
    }

    private fun checkIfExistInLocal(): Boolean {
        return File(SCA_JAR).exists()
    }

    private fun download() {
        log.info("start download scan_git tool")
        val downloadUrl = "$host/$SCA_JAR"
        FileOperator.download(URL(downloadUrl), File(SCA_JAR))
        log.info("downloaded scan_git tool")
        copyIntoSystemRoot()
    }

    private fun scan(cmd: List<String>) {
        Processor.executeWithLogs(ProcessBuilder(cmd), systemRoot, logStream)
    }
}

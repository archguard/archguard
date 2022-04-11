package com.thoughtworks.archguard.scanner.domain.scanner.diff

import com.thoughtworks.archguard.scanner.infrastructure.FileOperator
import com.thoughtworks.archguard.scanner.infrastructure.command.Processor
import com.thoughtworks.archguard.scanner.infrastructure.command.StreamConsumer
import org.slf4j.LoggerFactory
import java.io.File
import java.net.URL

class DiffChangesTool(
    val systemRoot: File,
    val systemId: Long,
    val language: String,
    val dbUrl: String,
    val branch: String,
    val logStream: StreamConsumer,
    val additionArguments: List<String>
) {
    private val log = LoggerFactory.getLogger(DiffChangesTool::class.java)
    private val host = "https://github.com/archguard/scanner/releases/download/v1.4.5"
    private val version = "1.4.5"
    private val SCAN_SOURCECODE_JAR = "diff_changes-$version-all.jar"

    fun analyse() {
        prepareTool()

        val cmd = mutableListOf(
            "java",
            "-jar",
            "-Ddburl=$dbUrl?useSSL=false",
            "diff_changes.jar",
            "--path=.",
            "--system-id=$systemId",
            "--language=${language.lowercase()}"
        )

        cmd.addAll(this.additionArguments)

        scan(cmd)
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
        log.info("copy SourceCode jar tool from local")
        FileOperator.copyTo(File(SCAN_SOURCECODE_JAR), File("$systemRoot/diff_changes.jar"))
        try {
            val chmod = ProcessBuilder("chmod", "+x", "diff_changes.jar")
            chmod.directory(systemRoot)
            chmod.start().waitFor()
        }catch (ex:Exception) {
            log.warn("chmod +x diff_changes.jar tool Exception")
        }
    }

    private fun checkIfExistInLocal(): Boolean {
        return File(SCAN_SOURCECODE_JAR).exists()
    }

    private fun download() {
        log.info("start download diff_changes Tool")
        val downloadUrl = "$host/$SCAN_SOURCECODE_JAR"
        FileOperator.download(URL(downloadUrl), File(SCAN_SOURCECODE_JAR))
        log.info("downloaded diff_changes Tool")
        copyIntoSystemRoot()
    }

    private fun scan(cmd: List<String>) {
        Processor.executeWithLogs(ProcessBuilder(cmd), systemRoot, logStream)
    }

}

package com.thoughtworks.archguard.scanner.domain.scanner.javaext.tbs

import com.thoughtworks.archguard.scanner.domain.tools.TestBadSmellReport
import com.thoughtworks.archguard.scanner.infrastructure.FileOperator
import com.thoughtworks.archguard.scanner.infrastructure.command.Processor
import com.thoughtworks.archguard.scanner.infrastructure.command.StreamConsumer
import org.slf4j.LoggerFactory
import java.io.File
import java.net.URL

class TestBadsmellTool(val systemRoot: File, val logStream: StreamConsumer, val scannerVersion: String) : TestBadSmellReport {
    private val host = "https://github.com/archguard/scanner/releases/download/v$scannerVersion"
    private val log = LoggerFactory.getLogger(TestBadsmellTool::class.java)
    private val SCAN_TEST_BADSMELL_JAR = "scan_test_badsmell-$scannerVersion-all.jar"

    private fun prepareTool() {
        val jarExist = checkIfExistInLocal()
        if (jarExist) {
            copyIntoSystemRoot()
        } else {
            download()
        }
    }

    override fun getTestBadSmellReport(): File? {
        prepareTool()
        scan(listOf("java", "-jar", "scan_test_badsmell.jar", "--path=."))
        val report = File("$systemRoot/bs.json")
        return if (report.exists()) {
            report
        } else {
            log.error("failed to get test bad smell")
            null
        }
    }

    private fun scan(cmd: List<String>) {
        Processor.executeWithLogs(ProcessBuilder(cmd), systemRoot, logStream)
    }

    private fun copyIntoSystemRoot() {
        log.info("copy jar tool from local")
        FileOperator.copyTo(File(SCAN_TEST_BADSMELL_JAR), File("$systemRoot/scan_test_badsmell.jar"))
        try {
            val chmod = ProcessBuilder("chmod", "+x", "scan_git.jar")
            chmod.directory(systemRoot)
            chmod.start().waitFor()
        } catch (ex: Exception) {
            log.warn("chmod +x scan_git.jar tool Exception")
        }
    }

    private fun checkIfExistInLocal(): Boolean {
        return File(SCAN_TEST_BADSMELL_JAR).exists()
    }

    private fun download() {
        log.info("start download scan_test_badsmell tool")
        val downloadUrl = "$host/scan_test_badsmell-$scannerVersion-all.jar"
        FileOperator.download(URL(downloadUrl), File(SCAN_TEST_BADSMELL_JAR))
        log.info("downloaded scan_test_badsmell tool")
        copyIntoSystemRoot()
    }
}

package com.thoughtworks.archguard.scanner.domain.scanner.bak.tbs

import com.thoughtworks.archguard.scanner.domain.tools.TestBadSmellReport
import com.thoughtworks.archguard.scanner.infrastructure.FileOperator
import com.thoughtworks.archguard.scanner.infrastructure.Processor
import org.slf4j.LoggerFactory
import java.io.File
import java.net.URL

class TestBadsmellTool(val systemRoot: File) : TestBadSmellReport {

    private val host = "https://github.com/archguard/scanner/releases/download/v1.1.4"
    private val version = "1.1.4"
    private val log = LoggerFactory.getLogger(TestBadsmellTool::class.java)

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
        Processor.executeWithLogs(ProcessBuilder(cmd), systemRoot)
    }


    private fun copyIntoSystemRoot() {
        log.info("copy jar tool from local")
        FileOperator.copyTo(File("scan_test_badsmell-$version-all.jar"), File("$systemRoot/scan_scan_test_badsmell.jar"))
        val chmod = ProcessBuilder("chmod", "+x", "scan_git.jar")
        chmod.directory(systemRoot)
        chmod.start().waitFor()
    }

    private fun checkIfExistInLocal(): Boolean {
        return File("scan_git-$version-all.jar").exists()
    }

    private fun download() {
        val downloadUrl = "$host/scan_test_badsmell-$version-all.jar"
        FileOperator.download(URL(downloadUrl), File("$systemRoot/scan_test_badsmell.jar"))
        val chmod = ProcessBuilder("chmod", "+x", "scan_git.jar")
        chmod.directory(systemRoot)
        chmod.start().waitFor()
    }
}

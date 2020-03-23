package com.thoughtworks.archgard.scanner.domain.toolscanners

import com.thoughtworks.archgard.scanner.infrastructure.FileOperator
import com.thoughtworks.archgard.scanner.infrastructure.Processor
import java.io.File
import java.net.URL

class CocaScanner(val projectRoot: File) : BadSmellReport, TestBadSmellReport {

    override fun getBadSmellReport(): String {
        try {
            download()
            scan(listOf("./coca", "bs", "-s", "type"))
            val report = File(projectRoot.toString() + "/coca_reporter/bs.json")
            val badSmellReport = report.readText()
            return badSmellReport
        } catch (ex: Exception) {
            return "{}"
        }
    }

    override fun getTestBadSmellReport(): String {
        try {
            download()
            scan(listOf("./coca", "tbs"))
            val report = File(projectRoot.toString() + "/coca_reporter/tbs.json")
            val testBadSmellReport = report.readText()
            return testBadSmellReport
        } catch (ex: Exception) {
            return "[]"
        }
    }

    private fun scan(cmd: List<String>) {
        Processor.executeWithLogs(ProcessBuilder(cmd), projectRoot)
    }

    private fun download() {
        val system = System.getProperty("os.name").toLowerCase()
        val downloadUrl =
                if (system.indexOf("mac") >= 0) {
                    "http://ci.archguard.org/view/ThirdPartyTool/job/coca/lastSuccessfulBuild/artifact/coca_macos"
                } else {
                    "http://ci.archguard.org/view/ThirdPartyTool/job/coca/lastSuccessfulBuild/artifact/coca_linux"
                }

        FileOperator.download(URL(downloadUrl), File(projectRoot.toString() + "/coca"))
        val chmod = ProcessBuilder("chmod", "+x", "coca")
        chmod.directory(projectRoot)
        chmod.start().waitFor()
    }
}
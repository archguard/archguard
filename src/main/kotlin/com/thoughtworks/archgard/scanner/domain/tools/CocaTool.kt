package com.thoughtworks.archgard.scanner.domain.tools

import com.thoughtworks.archgard.scanner.infrastructure.FileOperator
import com.thoughtworks.archgard.scanner.infrastructure.Processor
import org.slf4j.LoggerFactory
import java.io.File
import java.net.URL

class CocaTool(val projectRoot: File) : BadSmellReport, TestBadSmellReport {

    private val log = LoggerFactory.getLogger(CocaTool::class.java)

    override fun getBadSmellReport(): File? {
        download()
        scan(listOf("./coca", "bs", "-s", "type"))
        val report = File(projectRoot.toString() + "/coca_reporter/bs.json")
        return if (report.exists()) {
            report
        } else {
            log.error("failed to get bad smell")
            null
        }
    }

    override fun getTestBadSmellReport(): File? {
        download()
        scan(listOf("./coca", "tbs"))
        val report = File(projectRoot.toString() + "/coca_reporter/tbs.json")
        return if (report.exists()) {
            report
        } else {
            log.error("failed to get test bad smell")
            null
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
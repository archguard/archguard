package com.thoughtworks.archgard.scanner.domain.toolscanners

import com.thoughtworks.archgard.scanner.infrastructure.FileDownloader
import java.io.File
import java.io.IOException
import java.net.URL

class CocaScanner(val projectRoot: File?) : BadSmellReport, TestBadSmellReport {

    override fun getBadSmellReport(): String {
        download()
        scan(listOf("./coca", "bs", "-s", "type"))
        val report = File(projectRoot.toString() + "/coca_reporter/bs.json")
        val badSmellReport = report.readText()
        clean(report)
        return badSmellReport
    }

    override fun getTestBadSmellReport(): String {
        download()
        scan(listOf("./coca", "tbs"))
        val report = File(projectRoot.toString() + "/coca_reporter/tbs.json")
        val testBadSmellReport = report.readText()
        clean(report)
        return testBadSmellReport
    }

    @Throws(IOException::class, InterruptedException::class)
    private fun scan(cmd: List<String>) {
        val badSmell = ProcessBuilder(cmd)
        badSmell.directory(projectRoot)
        badSmell.start().waitFor()
    }

    private fun clean(report: File) {
        File(projectRoot.toString() + "/coca").delete()
        report.delete()
    }

    private fun download() {
        val system = System.getProperty("os.name").toLowerCase()
        val downloadUrl =
                if (system.indexOf("mac") >= 0) {
                    "http://ci.archguard.org/view/ThirdPartyTool/job/coca/lastSuccessfulBuild/artifact/coca_macos"
                } else {
                    "http://ci.archguard.org/view/ThirdPartyTool/job/coca/lastSuccessfulBuild/artifact/coca_linux"
                }

        FileDownloader.download(URL(downloadUrl), File(projectRoot.toString() + "/coca"))
        val chmod = ProcessBuilder("chmod", "+x", "coca")
        chmod.directory(projectRoot)
        chmod.start().waitFor()
    }
}
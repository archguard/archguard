package com.thoughtworks.archgard.scanner.domain.toolscanners

import com.thoughtworks.archgard.scanner.infrastructure.FileDownloader
import java.io.File
import java.io.IOException
import java.net.URL

class CocaScanner(val latestCocaUrl: String, val projectRoot: File?) : BadSmellReport {

    override fun getBadSmellReport(): String {
        download()
        scan("./coca bs -s type")
        val badSmellReport = File(projectRoot.toString() + "/coca_reporter/bs.json").readText()
        return badSmellReport
    }

    @Throws(IOException::class, InterruptedException::class)
    private fun scan(cmd: String) {
        val badSmell = ProcessBuilder(cmd)
        badSmell.directory(projectRoot)
        badSmell.start().waitFor()
    }

    private fun clean() {
        File(projectRoot.toString() + "/coca").delete()
        File(projectRoot.toString() + "/coca_reporter/bs.json").delete()
    }

    private fun download() {
        FileDownloader.download(URL(latestCocaUrl), File(projectRoot.toString() + "/coca"))
        val chmod = ProcessBuilder("chmod", "+x", "coca")
        chmod.directory(projectRoot)
        chmod.start().waitFor()
    }

}
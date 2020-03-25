package com.thoughtworks.archgard.scanner.domain.tools

import com.thoughtworks.archgard.scanner.infrastructure.FileOperator
import com.thoughtworks.archgard.scanner.infrastructure.Processor
import org.slf4j.LoggerFactory
import java.io.File
import java.net.URL

class GitScannerTool(val projectRoot: File, val branch: String) : GitReport {

    private val log = LoggerFactory.getLogger(GitScannerTool::class.java)

    override fun getGitReport(): File? {
        download()
        scan(listOf("java", "-jar", "scan_git.jar", "--git-path=.", "--branch=" + branch))
        val report = File(projectRoot.toString() + "/output.sql")
        return if (report.exists()) {
            report
        } else {
            log.info("failed to get output.sql")
            null
        }
    }

    private fun download() {
        val downloadUrl = "http://ci.archguard.org/job/scan-git/lastSuccessfulBuild/artifact/scan_git/target/scan_git-1.0-SNAPSHOT-jar-with-dependencies.jar"
        FileOperator.download(URL(downloadUrl), File(projectRoot.toString() + "/scan_git.jar"))
        val chmod = ProcessBuilder("chmod", "+x", "scan_git.jar")
        chmod.directory(projectRoot)
        chmod.start().waitFor()
    }

    private fun scan(cmd: List<String>) {
        Processor.executeWithLogs(ProcessBuilder(cmd), projectRoot)
    }

}
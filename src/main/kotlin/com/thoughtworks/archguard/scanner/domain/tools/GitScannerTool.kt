package com.thoughtworks.archguard.scanner.domain.tools

import com.thoughtworks.archguard.scanner.infrastructure.FileOperator
import com.thoughtworks.archguard.scanner.infrastructure.Processor
import org.slf4j.LoggerFactory
import java.io.File
import java.net.URL

class GitScannerTool(val systemRoot: File, val branch: String?, val systemId: Long, val repo: String) : GitReport {

    private val log = LoggerFactory.getLogger(GitScannerTool::class.java)
    private val host = "https://github.com/archguard/scanner/releases/download/v1.1.3"
    private val version = "1.1.3"

    override fun getGitReport(): File? {
        prepareTool()
        scan(listOf("java", "-jar", "scan_git.jar", "--path=.", "--branch=$branch",
                "--repo-id=$repo", "--system-id=$systemId"))
        val report = File("$systemRoot/output.sql")
        return if (report.exists()) {
            report
        } else {
            log.info("failed to get output.sql")
            null
        }
    }

    fun getLocReport(): File? {
        prepareTool()
        scan(listOf("java", "-jar", "scan_git.jar", "--path=.", "--loc=true",
                "--repo-id=$repo", "--system-id=$systemId"))
        val report = File("$systemRoot/loc_output.sql")
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
        FileOperator.copyTo(File("scan_git-$version-all.jar"), File(systemRoot.toString() + "/scan_git.jar"))
        val chmod = ProcessBuilder("chmod", "+x", "scan_git.jar")
        chmod.directory(systemRoot)
        chmod.start().waitFor()
    }

    private fun checkIfExistInLocal(): Boolean {
        return File("scan_git-$version-all.jar").exists()
    }

    private fun download() {
        val downloadUrl = "$host/scan_git-$version-all.jar"
        FileOperator.download(URL(downloadUrl), File("$systemRoot/scan_git.jar"))
        val chmod = ProcessBuilder("chmod", "+x", "scan_git.jar")
        chmod.directory(systemRoot)
        chmod.start().waitFor()
    }

    private fun scan(cmd: List<String>) {
        Processor.executeWithLogs(ProcessBuilder(cmd), systemRoot)
    }

}

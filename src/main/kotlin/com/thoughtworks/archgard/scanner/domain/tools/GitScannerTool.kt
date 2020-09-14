package com.thoughtworks.archgard.scanner.domain.tools

import com.thoughtworks.archgard.scanner.infrastructure.FileOperator
import com.thoughtworks.archgard.scanner.infrastructure.Processor
import org.slf4j.LoggerFactory
import java.io.File
import java.net.URL

class GitScannerTool(val systemRoot: File, val branch: String) : GitReport {

    private val log = LoggerFactory.getLogger(GitScannerTool::class.java)
    private val host = "ec2-68-79-38-105.cn-northwest-1.compute.amazonaws.com.cn:8080"

    override fun getGitReport(): File? {
        prepareTool()
        scan(listOf("java", "-jar", "scan_git.jar", "--git-path=.", "--branch=" + branch))
        val report = File(systemRoot.toString() + "/output.sql")
        return if (report.exists()) {
            report
        } else {
            log.info("failed to get output.sql")
            null
        }
    }

    override fun getGitCommitFrequentModifiedFileReport(): File? {
        val reportPath = systemRoot.toString() + "/frequent_modified_file.txt"
        scan(listOf("git", "log", "--name-only", "--oneline", "--pretty='format:'", " > ", reportPath))
        val report = File(reportPath)
        return if (report.exists()) {
            report
        } else {
            log.info("failed to get frequent_modified_file.txt")
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
        FileOperator.copyTo(File("scan_git-1.0-SNAPSHOT-jar-with-dependencies.jar"), File(systemRoot.toString() + "/scan_git.jar"))
        val chmod = ProcessBuilder("chmod", "+x", "scan_git.jar")
        chmod.directory(systemRoot)
        chmod.start().waitFor()
    }

    private fun checkIfExistInLocal(): Boolean {
        return File("scan_git-1.0-SNAPSHOT-jar-with-dependencies.jar").exists()
    }

    private fun download() {
        val downloadUrl = "http://$host/job/code-scanners/lastSuccessfulBuild/artifact/scan_git/target/scan_git-1.0-SNAPSHOT-jar-with-dependencies.jar"
        FileOperator.download(URL(downloadUrl), File("$systemRoot/scan_git.jar"))
        val chmod = ProcessBuilder("chmod", "+x", "scan_git.jar")
        chmod.directory(systemRoot)
        chmod.start().waitFor()
    }

    private fun scan(cmd: List<String>) {
        Processor.executeWithLogs(ProcessBuilder(cmd), systemRoot)
    }

}

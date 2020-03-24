package com.thoughtworks.archgard.scanner.domain.toolscanners

import com.thoughtworks.archgard.scanner.infrastructure.FileOperator
import com.thoughtworks.archgard.scanner.infrastructure.Processor
import java.io.File
import java.net.URL

class GitScanner(val projectRoot: File, val branch: String): GitReport {

    override fun getGitReport(): File? {
        download()
        scan(listOf("java", "-jar", "scan_git.jar", "--git-path=.", "--branch=" + branch))
        return File(projectRoot.toString() + "output.sql")
    }

    private fun download() {
        val downloadUrl = "http://ci.archguard.org/job/code-scanners/lastSuccessfulBuild/artifact/scan_git/target/scan_git-1.0-SNAPSHOT-jar-with-dependencies.jar"
        FileOperator.download(URL(downloadUrl), File(projectRoot.toString() + "/scan_git.jar"))
        val chmod = ProcessBuilder("chmod", "+x", "scan_git.jar")
        chmod.directory(projectRoot)
        chmod.start().waitFor()
    }

    private fun scan(cmd: List<String>) {
        Processor.executeWithLogs(ProcessBuilder(cmd), projectRoot)
    }

}
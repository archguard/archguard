package com.thoughtworks.archgard.scanner.domain.tools

import com.thoughtworks.archgard.scanner.domain.project.BuildTool
import com.thoughtworks.archgard.scanner.infrastructure.FileOperator
import com.thoughtworks.archgard.scanner.infrastructure.Processor
import org.slf4j.LoggerFactory
import java.io.File
import java.net.URL

class JacocoTool(val workspace: File, val projectRoot: File, val buildTool: BuildTool) {

    private val log = LoggerFactory.getLogger(JacocoTool::class.java)

    fun execToSql(): File? {
        prepareTool()
        call(listOf("java", "-jar", "scan_jacoco.jar", "--target-project=${projectRoot.absolutePath}",
                "--bin=${buildTool.target}/classes",
                "--exec=${buildTool.target}/jacoco.exec"))
        val sqlFile = File("${workspace.absolutePath}/jacoco.sql")
        return if (sqlFile.exists()) {
            sqlFile
        } else {
            log.info("failed to get jacoco.sql")
            null
        }
    }

    private fun prepareTool() {
        val jarExist = checkIfExistInLocal()
        if (jarExist) {
            copyIntoProjectRoot()
        } else {
            download()
        }
    }

    private fun copyIntoProjectRoot() {
        log.info("copy jar tool from local")
        FileOperator.copyTo(File("scan_jacoco-1.0-SNAPSHOT-jar-with-dependencies.jar"), File(workspace.absolutePath.toString() + "/scan_jacoco.jar"))
        val chmod = ProcessBuilder("chmod", "+x", "scan_jacoco.jar")
        chmod.directory(workspace)
        chmod.start().waitFor()
    }

    private fun checkIfExistInLocal(): Boolean {
        return File("scan_jacoco-1.0-SNAPSHOT-jar-with-dependencies.jar").exists()
    }

    private fun download() {
        if (File(workspace.absolutePath + "/scan_jacoco.jar").exists()) {
            return
        }
        val jarLink = "http://ci.archguard.org/job/code-scanners/lastSuccessfulBuild/artifact/scan_jacoco/target/scan_jacoco-1.0-SNAPSHOT-jar-with-dependencies.jar"
        FileOperator.download(URL(jarLink), File(workspace.absolutePath + "/scan_jacoco.jar"))
        val chmod = ProcessBuilder("chmod", "+x", "scan_jacoco.jar")
        chmod.directory(workspace)
        chmod.start().waitFor()
    }

    private fun call(cmd: List<String>) {
        Processor.executeWithLogs(ProcessBuilder(cmd), workspace)
    }
}
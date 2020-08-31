package com.thoughtworks.archgard.scanner.domain.tools

import com.thoughtworks.archgard.scanner.infrastructure.FileOperator
import com.thoughtworks.archgard.scanner.infrastructure.Processor
import org.slf4j.LoggerFactory
import java.io.File
import java.net.URL

class InvokeSqlTool(val systemRoot: File) {
    private val log = LoggerFactory.getLogger(InvokeSqlTool::class.java)
    private val host = "ec2-68-79-38-105.cn-northwest-1.compute.amazonaws.com.cn:8080"

    fun analyse(): List<File> {
        prepareTool()
        scan(listOf("java", "-jar", "invokes_plsql.jar", "."))
        return listOf(
                File(systemRoot.toString() + "/Procedure_INSERT.sql"),
                File(systemRoot.toString() + "/CALLEE_INSERT.sql"))
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
        FileOperator.copyTo(File("invokes_plsql-1.0-SNAPSHOT-jar-with-dependencies.jar"), File(systemRoot.toString() + "/invokes_plsql.jar"))
        val chmod = ProcessBuilder("chmod", "+x", "invokes_plsql.jar")
        chmod.directory(systemRoot)
        chmod.start().waitFor()
    }

    private fun checkIfExistInLocal(): Boolean {
        return File("invokes_plsql-1.0-SNAPSHOT-jar-with-dependencies.jar").exists()
    }

    private fun download() {
        val downloadUrl = "http://$host/job/code-scanners/lastSuccessfulBuild/artifact/invokes_plsql/target/invokes_plsql-1.0-SNAPSHOT-jar-with-dependencies.jar"
        FileOperator.download(URL(downloadUrl), File(systemRoot.toString() + "/invokes_plsql.jar"))
        val chmod = ProcessBuilder("chmod", "+x", "invokes_plsql.jar")
        chmod.directory(systemRoot)
        chmod.start().waitFor()
    }

    private fun scan(cmd: List<String>) {
        Processor.executeWithLogs(ProcessBuilder(cmd), systemRoot)
    }

}

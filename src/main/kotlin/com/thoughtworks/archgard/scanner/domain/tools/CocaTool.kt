package com.thoughtworks.archgard.scanner.domain.tools

import com.thoughtworks.archgard.scanner.infrastructure.FileOperator
import com.thoughtworks.archgard.scanner.infrastructure.Processor
import org.slf4j.LoggerFactory
import java.io.File
import java.net.URL

class CocaTool(val systemRoot: File) : TestBadSmellReport {

    private val host = "ec2-68-79-38-105.cn-northwest-1.compute.amazonaws.com.cn:8080"
    private val log = LoggerFactory.getLogger(CocaTool::class.java)

    private fun prepareTool() {
        val jarExist = checkIfExistInLocal()
        if (jarExist) {
            copyIntoSystemRoot()
        } else {
            download()
        }
    }

    private fun copyIntoSystemRoot() {
        val system = System.getProperty("os.name").toLowerCase()
        if (system.indexOf("mac") >= 0) {
            log.info("copy coca_macos jar tool from local")
            FileOperator.copyTo(File("coca_macos"), File(systemRoot.toString() + "/coca"))
        } else {
            log.info("copy coca_linux jar tool from local")
            FileOperator.copyTo(File("coca_linux"), File(systemRoot.toString() + "/coca"))
        }
        val chmod = ProcessBuilder("chmod", "+x", "coca")
        chmod.directory(systemRoot)
        chmod.start().waitFor()
    }

    private fun checkIfExistInLocal(): Boolean {
        val system = System.getProperty("os.name").toLowerCase()
        if (system.indexOf("mac") >= 0) {
            return File("coca_macos").exists()
        } else {
            return File("coca_linux").exists()
        }
    }

    fun getBadSmellReport(): File? {
        prepareTool()
        scan(listOf("./coca", "bs", "-s", "type"))
        val report = File(systemRoot.toString() + "/coca_reporter/bs.json")
        return if (report.exists()) {
            report
        } else {
            log.error("failed to get bad smell")
            null
        }
    }

    override fun getTestBadSmellReport(): File? {
        prepareTool()
        scan(listOf("./coca", "tbs"))
        val report = File(systemRoot.toString() + "/coca_reporter/tbs.json")
        return if (report.exists()) {
            report
        } else {
            log.error("failed to get test bad smell")
            null
        }
    }

    private fun scan(cmd: List<String>) {
        Processor.executeWithLogs(ProcessBuilder(cmd), systemRoot)
    }

    private fun download() {
        val system = System.getProperty("os.name").toLowerCase()
        val downloadUrl =
                if (system.indexOf("mac") >= 0) {
                    "http://$host/job/coca/lastSuccessfulBuild/artifact/coca_macos"
                } else {
                    "http://$host/job/coca/lastSuccessfulBuild/artifact/coca_linux"
                }

        FileOperator.download(URL(downloadUrl), File(systemRoot.toString() + "/coca"))
        val chmod = ProcessBuilder("chmod", "+x", "coca")
        chmod.directory(systemRoot)
        chmod.start().waitFor()
    }
}

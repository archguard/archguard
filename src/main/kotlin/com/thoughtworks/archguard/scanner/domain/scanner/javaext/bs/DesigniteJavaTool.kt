package com.thoughtworks.archguard.scanner.domain.tools

import com.thoughtworks.archguard.scanner.infrastructure.FileOperator
import com.thoughtworks.archguard.scanner.infrastructure.command.Processor
import com.thoughtworks.archguard.scanner.infrastructure.command.StreamConsumer
import org.slf4j.LoggerFactory
import java.io.File
import java.net.URL
import java.nio.file.Files
import java.nio.file.StandardCopyOption

class DesigniteJavaTool(val systemRoot: File, val logStream: StreamConsumer) {
    private val log = LoggerFactory.getLogger(DesigniteJavaTool::class.java)
    private val host = "https://github.com/archguard/scanner/releases/download/v1.1.3"
    private val SCAN_DESIGNITE_JAR = "DesigniteJava.jar"

    fun readReport(designiteJavaReportType: DesigniteJavaReportType): List<String> {
        prepareTool()
        return getTargetScannedDirections(systemRoot)
            .map { generateReport(designiteJavaReportType, it)?.readLines() }
            .filterNotNull()
            .flatten()
            .filter { !it.contains("Project Name") }
    }

    private fun generateReport(type: DesigniteJavaReportType, currentScannedDirection: String): File? {
        process(currentScannedDirection)
        val report = File(currentScannedDirection + "/DesigniteReport/${type.reportName}")
        return if (report.exists()) report else null
    }

    private fun process(currentScannedDirection: String) {
        scan(
            listOf(
                "java",
                "-jar",
                "-Xmx1G",
                "${systemRoot.absolutePath}/$SCAN_DESIGNITE_JAR",
                "-i",
                currentScannedDirection,
                "-o",
                "$currentScannedDirection/DesigniteReport"
            )
        )
    }

    private fun prepareTool() {
        val file = File(SCAN_DESIGNITE_JAR)
        when {
            file.exists() -> {
                log.info("DesigniteJava.jar already exists in systemRoot")
                Files.copy(
                    file.toPath(),
                    File("$systemRoot/$SCAN_DESIGNITE_JAR").toPath(),
                    StandardCopyOption.REPLACE_EXISTING
                )
            }
            checkIfExistInLocal() -> {
                log.info("DesigniteJava.jar exists in local")
                Files.copy(
                    File("DesigniteJava.jar").toPath(),
                    File("$systemRoot/$SCAN_DESIGNITE_JAR").toPath(),
                    StandardCopyOption.REPLACE_EXISTING
                )
            }
            else -> {
                log.info("Download DesigniteJava.jar from remote")
                val downloadUrl = "$host/$SCAN_DESIGNITE_JAR"
                FileOperator.download(URL(downloadUrl), File(SCAN_DESIGNITE_JAR))
            }
        }
        try {
            val chmod = ProcessBuilder("chmod", "+x", SCAN_DESIGNITE_JAR)
            chmod.directory(systemRoot)
            chmod.start().waitFor()
        }catch (ex:Exception) {
            log.warn("chmod +x {} tool Exception", SCAN_DESIGNITE_JAR)
        }
    }

    private fun checkIfExistInLocal(): Boolean {
        return File("DesigniteJava.jar").exists()
    }

    private fun scan(cmd: List<String>) {
        Processor.executeWithLogs(ProcessBuilder(cmd), systemRoot, logStream)
    }

    private fun getTargetScannedDirections(workspace: File): List<String> {
        val target = workspace.walkTopDown()
            .filter { f -> checkIsSubModulePath(f) }
            .map { f -> f.absolutePath }
            .distinct()
            .toList()
        return if (target.size > 1) {
            target.filter { it != workspace.absolutePath }
        } else {
            target
        }
    }

    private fun checkIsSubModulePath(f: File): Boolean {
        val list = f.list() ?: return false
        if (list.contains("src") && list.any { it.contains("pom.xml") || it.contains("build.gradle") }) {
            return true
        }
        return false
    }

}

enum class DesigniteJavaReportType(val reportName: String) {
    TYPE_METRICS("typeMetrics.csv"), METHOD_METRICS("methodMetrics.csv"), BAD_SMELL_METRICS("designCodeSmells.csv")
}
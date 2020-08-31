package com.thoughtworks.archgard.scanner.domain.tools

import com.thoughtworks.archgard.scanner.infrastructure.FileOperator
import com.thoughtworks.archgard.scanner.infrastructure.Processor
import org.slf4j.LoggerFactory
import java.io.File
import java.net.URL
import java.nio.file.Files
import java.nio.file.StandardCopyOption

class DesigniteJavaTool(val systemRoot: File) {
    private val log = LoggerFactory.getLogger(DesigniteJavaTool::class.java)
    private val host = "ec2-68-79-38-105.cn-northwest-1.compute.amazonaws.com.cn:8080"

    fun getBadSmellReport(): List<String> {
        return getTargetFile(systemRoot).map { getBadSmellReport(it)?.readLines() }
                .filterNotNull()
                .flatten()
                .filter { !it.contains("Project Name") }
    }

    fun getTypeMetricsReport(): List<String> {
        return getTargetFile(systemRoot)
                .map { getTypeMetricsReport(it)?.readLines() }
                .filterNotNull()
                .flatten()
                .filter { !it.contains("Project Name") }
    }

    fun getMethodMetricsReport(): List<String> {
        return getTargetFile(systemRoot)
                .map { getMethodMetricsReport(it)?.readLines() }
                .filterNotNull()
                .flatten()
                .filter { !it.contains("Project Name") }
    }

    private fun getBadSmellReport(target: File): File? {
        val report = File(target.toString() + "/DesigniteReport/designCodeSmells.csv")
        process(target)
        return if (report.exists()) {
            report
        } else {
            null
        }
    }

    private fun getTypeMetricsReport(target: File): File? {
        val report = File(target.toString() + "/DesigniteReport/typeMetrics.csv")
        process(target)
        return if (report.exists()) {
            report
        } else {
            null
        }
    }

    private fun getMethodMetricsReport(target: File): File? {
        val report = File(target.toString() + "/DesigniteReport/methodMetrics.csv")
        process(target)
        return if (report.exists()) {
            report
        } else {
            null
        }
    }

    private fun process(target: File) {
        prepareTool()
        scan(listOf("java", "-jar", "-Xmx1G", "${systemRoot.absolutePath}/DesigniteJava.jar", "-i", target.absolutePath, "-o", "${target.absolutePath}/DesigniteReport"))
    }

    private fun prepareTool() {
        val file = File(systemRoot.toString() + "/DesigniteJava.jar")
        if (file.exists()) {
            log.info("DesigniteJava.jar already exists in systemRoot")
            Files.copy(file.toPath(),
                    File(systemRoot.toString() + "/DesigniteJava.jar").toPath(),
                    StandardCopyOption.REPLACE_EXISTING)
        } else if (checkIfExistInLocal()) {
            log.info("DesigniteJava.jar exists in local")
            Files.copy(File("DesigniteJava.jar").toPath(),
                    File(systemRoot.toString() + "/DesigniteJava.jar").toPath(),
                    StandardCopyOption.REPLACE_EXISTING)
        } else {
            log.info("Download DesigniteJava.jar from remote")
            val downloadUrl = "http://$host/job/DesigniteJava/lastSuccessfulBuild/artifact/target/DesigniteJava.jar"
            FileOperator.download(URL(downloadUrl), File(systemRoot.toString() + "/DesigniteJava.jar"))
        }

        val chmod = ProcessBuilder("chmod", "+x", "DesigniteJava.jar")
        chmod.directory(systemRoot)
        chmod.start().waitFor()
    }

    private fun checkIfExistInLocal(): Boolean {
        return File("DesigniteJava.jar").exists()
    }

    private fun scan(cmd: List<String>) {
        Processor.executeWithLogs(ProcessBuilder(cmd), systemRoot)
    }

    private fun getTargetFile(workspace: File): List<File> {
        val target = workspace.walkTopDown()
                .filter { f -> f.absolutePath.endsWith("pom.xml") || f.absolutePath.endsWith("build.gradle") }
                .map { f -> f.parentFile }
                .distinct()
                .toList()
        if (target.size > 1) {
            return target.filter { it.absolutePath != workspace.absolutePath }
        } else {
            return target
        }
    }

}
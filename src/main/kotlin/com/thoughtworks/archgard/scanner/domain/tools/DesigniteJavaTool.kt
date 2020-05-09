package com.thoughtworks.archgard.scanner.domain.tools

import com.thoughtworks.archgard.scanner.infrastructure.FileOperator
import com.thoughtworks.archgard.scanner.infrastructure.Processor
import java.io.File
import java.net.URL
import java.nio.file.Files
import java.nio.file.StandardCopyOption

class DesigniteJavaTool(val projectRoot: File) {

    fun getBadSmellReport(): List<String> {
        return getTargetFile(projectRoot).map { getBadSmellReport(it)?.readLines() }
                .filterNotNull()
                .flatten()
                .filter { !it.contains("Project Name") }
    }

    fun getTypeMetricsReport(): List<String> {
        return getTargetFile(projectRoot)
                .map { getTypeMetricsReport(it)?.readLines() }
                .filterNotNull()
                .flatten()
                .filter { !it.contains("Project Name") }
    }

    private fun getBadSmellReport(target: File): File? {
        val report = File(projectRoot.toString() + "/designCodeSmells.csv")
        process(target)
        return if (report.exists()) {
            report
        } else {
            null
        }
    }

    private fun getTypeMetricsReport(target: File): File? {
        val report = File(projectRoot.toString() + "/typeMetrics.csv")
        process(target)
        return if (report.exists()) {
            report
        } else {
            null
        }
    }

    private fun process(target: File) {
        download(target)
        scan(listOf("java", "-jar", "-Xmx", "${target.absolutePath}/DesigniteJava.jar", "-i", target.absolutePath, "-o", "."))
    }

    private fun download(target: File) {
        val file = File(projectRoot.toString() + "/DesigniteJava.jar")
        if (file.exists()) {
            Files.copy(file.toPath(),
                    File(target.toString() + "/DesigniteJava.jar").toPath(),
                    StandardCopyOption.REPLACE_EXISTING)
            return
        }
        val downloadUrl = "http://ci.archguard.org/job/DesigniteJava/lastSuccessfulBuild/artifact/target/DesigniteJava.jar"
        FileOperator.download(URL(downloadUrl), file)
        Files.copy(file.toPath(),
                File(target.toString() + "/DesigniteJava.jar").toPath(),
                StandardCopyOption.REPLACE_EXISTING)

        val chmod = ProcessBuilder("chmod", "+x", "DesigniteJava.jar")
        chmod.directory(target)
        chmod.start().waitFor()
    }

    private fun scan(cmd: List<String>) {
        Processor.executeWithLogs(ProcessBuilder(cmd), projectRoot)
    }

    private fun getTargetFile(workspace: File): List<File> {
        val target = workspace.walkTopDown()
                .filter { f -> f.absolutePath.endsWith("pom.xml") || f.absolutePath.endsWith("build.gradle") }
                .map { f -> f.parentFile.parentFile }
                .distinct()
                .toList()
        if (target.size > 1) {
            return target.filter { it.absolutePath != workspace.absolutePath }
        } else {
            return target
        }
    }

}
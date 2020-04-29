package com.thoughtworks.archgard.scanner.domain.tools

import com.thoughtworks.archgard.scanner.infrastructure.FileOperator
import com.thoughtworks.archgard.scanner.infrastructure.Processor
import java.io.File
import java.net.URL

class DesigniteJavaTool(val projectRoot: File) : BadSmellReport {

    override fun getBadSmellReport(): File? {
        val report = File(projectRoot.toString() + "/designCodeSmells.csv")
        if (report.exists()) {
            return report
        }
        process()
        return if (report.exists()) {
            report
        } else {
            null
        }
    }

    fun getTypeMetricsReport(): File? {
        val report = File(projectRoot.toString() + "/typeMetrics.csv")
        if (report.exists()) {
            return report
        }
        process()
        return if (report.exists()) {
            report
        } else {
            null
        }
    }

    private fun process() {
        download()
        scan(listOf("java", "-jar", "-Xmx1024m","DesigniteJava.jar", "-i", ".", "-o", "."))
    }


    private fun download() {
        val downloadUrl = "http://ci.archguard.org/job/DesigniteJava/lastSuccessfulBuild/artifact/target/DesigniteJava.jar"
        FileOperator.download(URL(downloadUrl), File(projectRoot.toString() + "/DesigniteJava.jar"))
        val chmod = ProcessBuilder("chmod", "+x", "DesigniteJava.jar")
        chmod.directory(projectRoot)
        chmod.start().waitFor()
    }

    private fun scan(cmd: List<String>) {
        Processor.executeWithLogs(ProcessBuilder(cmd), projectRoot)
    }


}
package com.thoughtworks.archgard.scanner.domain.toolscanners

import com.thoughtworks.archgard.scanner.infrastructure.FileOperator
import com.thoughtworks.archgard.scanner.infrastructure.Processor
import java.io.File
import java.net.URL

class JavaByteCodeScanner(val projectRoot: File, val dbUrl: String) : JavaDependencyReport {

    override fun getDependencyReport(): File? {
        download()
        scan(listOf("java", "-jar", "-Ddburl=" + dbUrl + "?useSSL=false", "scan_java_bytecode", "."))
        return null
    }

    private fun download() {
        val downloadUrl = "http://ci.archguard.org/job/code-scanners/lastSuccessfulBuild/artifact/scan_java_bytecode/target/scan_java_bytecode-1.2-jar-with-dependencies.jar"
        FileOperator.download(URL(downloadUrl), File(projectRoot.toString() + "/scan_java_bytecode.jar"))
        val chmod = ProcessBuilder("chmod", "+x", "scan_java_bytecode.jar")
        chmod.directory(projectRoot)
        chmod.start().waitFor()
    }

    private fun scan(cmd: List<String>) {
        Processor.executeWithLogs(ProcessBuilder(cmd), projectRoot)
    }

}
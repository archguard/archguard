package com.thoughtworks.archgard.scanner.domain.tools

import com.thoughtworks.archgard.scanner.infrastructure.FileOperator
import com.thoughtworks.archgard.scanner.infrastructure.Processor
import java.io.File
import java.net.URL

class InvokeSqlTool(val projectRoot: File) {
    fun analyse(): List<File> {
        download()
        scan(listOf("java", "-jar", "invokes_plsql.jar", "."))
        return listOf(File(projectRoot.toString() + "/CALLEE_INSERT.sql"),
                File(projectRoot.toString() + "/Procedure_INSERT.sql"))
    }

    private fun download() {
        val downloadUrl = "http://ci.archguard.org/job/code-scanners/lastSuccessfulBuild/artifact/invokes_plsql/target/invokes_plsql-1.0-SNAPSHOT-jar-with-dependencies.jar"
        FileOperator.download(URL(downloadUrl), File(projectRoot.toString() + "/invokes_plsql.jar"))
        val chmod = ProcessBuilder("chmod", "+x", "invokes_plsql.jar")
        chmod.directory(projectRoot)
        chmod.start().waitFor()
    }

    private fun scan(cmd: List<String>) {
        Processor.executeWithLogs(ProcessBuilder(cmd), projectRoot)
    }

}
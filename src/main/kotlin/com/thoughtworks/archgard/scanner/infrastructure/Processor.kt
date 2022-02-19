package com.thoughtworks.archgard.scanner.infrastructure

import org.slf4j.LoggerFactory
import java.io.BufferedReader
import java.io.File
import java.io.InputStream
import java.io.InputStreamReader

object Processor {
    private val log = LoggerFactory.getLogger(Processor::class.java)

    fun executeWithLogs(pb: ProcessBuilder, workspace: File): Int {
        return executeWithLogsAndAppendToFile(pb, workspace, null)
    }

    fun executeWithLogsAndAppendToFile(pb: ProcessBuilder, workspace: File, reportPath: String?): Int {
        pb.redirectErrorStream(true)
        pb.directory(workspace)
        if (reportPath != null) {
            pb.redirectOutput(File(reportPath))
        }
        val p = pb.start()
        val inputStream: InputStream = p.inputStream
        val bufferedReader = BufferedReader(InputStreamReader(inputStream, "gbk"))
        while (true) {
            val line = bufferedReader.readLine() ?: break
            log.info(line)
        }
        inputStream.close()
        p.waitFor()
        return p.exitValue()
    }
}
package com.thoughtworks.archguard.smartscanner.infra

import com.thoughtworks.archguard.scanner.infrastructure.command.StreamConsumer
import com.thoughtworks.archguard.smartscanner.ScannerClient
import com.thoughtworks.archguard.smartscanner.ScannerCommand
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

@Component
class ScannerCliClient : ScannerClient {
    private val logger = LoggerFactory.getLogger(this.javaClass)

    override fun send(command: ScannerCommand) {
        val cmd = mutableListOf("java", "-jar", FILE_NAME) + command.toArguments()
        logger.debug("cmd: $cmd")

        RemoteFileLoader.load(FILE_NAME, DOWNLOAD_URL, command.logStream)
        executeWithLogsAndAppendToFile(ProcessBuilder(cmd), command.logStream)
    }

    private fun executeWithLogsAndAppendToFile(pb: ProcessBuilder, logStream: StreamConsumer): Int {
        pb.redirectErrorStream(true)
        val p: Process = pb.start()
        val inputStream: InputStream = p.inputStream
        val bufferedReader = BufferedReader(InputStreamReader(inputStream, "gbk"))
        while (true) {
            val line = bufferedReader.readLine() ?: break
            logger.info(line)
            logStream.consumeLine(line)
        }

        inputStream.close()
        p.waitFor()
        return p.exitValue()
    }

    companion object {
        private const val VERSION = "2.1.5"
        private const val TAG = "v$VERSION"
        private const val RELEASE_REPO_URL = "https://github.com/archguard/archguard/releases/download/$TAG"
        private const val DOWNLOAD_URL = "$RELEASE_REPO_URL/scanner_cli-$VERSION-all.jar"
        private const val FILE_NAME = "scanner_cli.jar"
    }
}

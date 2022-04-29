package com.thoughtworks.archguard.smartscanner.infra

import com.thoughtworks.archguard.scanner.infrastructure.command.Processor
import com.thoughtworks.archguard.smartscanner.ScannerClient
import com.thoughtworks.archguard.smartscanner.ScannerCommand
import org.springframework.stereotype.Component

@Component
class ScannerCliClient : ScannerClient {
    override fun send(command: ScannerCommand) {
        val cmd = mutableListOf("java", "-jar", FILE_NAME) + command.toArguments()

        RemoteFileLoader.load(command.workspace, FILE_NAME, DOWNLOAD_URL)
        Processor.executeWithLogs(ProcessBuilder(cmd), command.workspace, command.logStream)
    }

    companion object {
        private const val VERSION = "1.7.0"
        private const val TAG = "plugin-ea-0.0.5"
        private const val RELEASE_REPO_URL = "https://github.com/archguard/scanner/releases/download/$TAG"
        private const val DOWNLOAD_URL = "$RELEASE_REPO_URL/scanner_cli-$VERSION-all.jar"
        private const val FILE_NAME = "scanner_cli.jar"
    }
}

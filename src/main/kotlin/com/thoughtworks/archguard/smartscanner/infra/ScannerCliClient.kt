package com.thoughtworks.archguard.smartscanner.infra

import com.thoughtworks.archguard.smartscanner.ScannerClient
import com.thoughtworks.archguard.smartscanner.ScannerCommand
import org.springframework.stereotype.Component

@Component
class ScannerCliClient : ScannerClient {
    override fun send(command: ScannerCommand) {
        /**
         * @see SourceCodeTool
         * manage, download, and execute with scanner_cli
         */
        TODO("Not yet implemented")
    }
}

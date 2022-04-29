package com.thoughtworks.archguard.smartscanner

import com.thoughtworks.archguard.scanner.domain.scanner.Scanner
import com.thoughtworks.archguard.scanner.domain.scanner.codescan.sourcecode.SourceCodeScanner
import com.thoughtworks.archguard.scanner.domain.scanner.javaext.bs.ScanContext
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

// replace the { HubExecutor, ScannerManager }
@Service
class StranglerScannerExecutor(
    @Autowired private val scanners: List<Scanner>,
    @Autowired private val scannerClient: ScannerClient,
) {
    @Value("\${server.port}")
    private val serverPort: String = "8080"

    private val log = LoggerFactory.getLogger(this.javaClass)

    fun execute(context: ScanContext) {
        scanners.filter { it.canScan(context) }.forEach { legacyScanner ->
            GlobalScope.launch { legacyScanner.scanWith(context) }
        }
    }

    private fun Scanner.scanWith(context: ScanContext) {
        when (this) {
            is SourceCodeScanner -> this@StranglerScannerExecutor.scan(context)
            else -> scan(context)
        }
    }

    private fun scan(context: ScanContext) {
        val command = ScannerCommand(
            type = AnalyserType.SOURCE_CODE,
            systemId = context.systemId.toString(),
            serverUrl = "http://localhost:$serverPort",
            path = context.codePath,
            workspace = context.workspace,
            logStream = context.logStream,
        ).also {
            it.language = context.language
            it.features = listOf("apicalls", "datamap")
        }

        scannerClient.send(command)
    }
}

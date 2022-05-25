package com.thoughtworks.archguard.smartscanner

import com.thoughtworks.archguard.scanner.domain.scanner.Scanner
import com.thoughtworks.archguard.scanner.domain.scanner.codescan.sca.ScaScanner
import com.thoughtworks.archguard.scanner.domain.scanner.codescan.sourcecode.SourceCodeScanner
import com.thoughtworks.archguard.scanner.domain.scanner.diff.DiffChangesScanner
import com.thoughtworks.archguard.scanner.domain.scanner.git.GitSourceScanner
import com.thoughtworks.archguard.scanner.domain.scanner.javaext.bs.ScanContext
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.archguard.scanner.core.context.AnalyserType
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
    @Value("\${server.port:8080}")
    private val serverPort: String = "8080"

    private val log = LoggerFactory.getLogger(this.javaClass)

    /**
     * batch execute, replace the legacy analyser if found the new structured analyser type
     */
    fun execute(context: ScanContext) {
        scanners.filter { it.canScan(context) }.forEach { legacyScanner ->
            GlobalScope.launch { legacyScanner.scanWith(context) }
        }
    }

    private fun Scanner.scanWith(context: ScanContext) {
        // TODO just delete and remove the disabled scanner
        if (!canScan(context)) return

        val command = when (this) {
            is SourceCodeScanner -> this@StranglerScannerExecutor.buildCommand(context, AnalyserType.SOURCE_CODE)
            is ScaScanner -> this@StranglerScannerExecutor.buildCommand(context, AnalyserType.SCA)
            is DiffChangesScanner -> this@StranglerScannerExecutor.buildCommand(context, AnalyserType.DIFF_CHANGES)
            is GitSourceScanner -> this@StranglerScannerExecutor.buildCommand(context, AnalyserType.GIT)
            else -> null
        }
        // TODO call multiple analyser in one call with DAG config
        if (command == null) scan(context)
        else {
            log.debug("found new implementation of ${this.javaClass}, execute command: $command")
            scannerClient.send(command)
        }
    }

    /**
     * run the specific analyser
     */
    fun run(context: ScanContext, type: AnalyserType) {
        buildCommand(context, type)?.let(scannerClient::send)
    }

    private fun buildCommand(context: ScanContext, type: AnalyserType): ScannerCommand? {
        val baseline = context.run {
            ScannerCommand(
                type = type,
                systemId = systemId.toString(),
                serverUrl = "http://localhost:$serverPort",
                path = codePath,
                workspace = workspace,
                logStream = logStream,
                branch = context.branch
            )
        }

        return when (type) {
            AnalyserType.SOURCE_CODE -> baseline.also {
                it.language = context.language
                it.features = listOf("apicalls", "datamap")
            }
            AnalyserType.SCA -> baseline.also {
                it.language = context.language
            }
            AnalyserType.DIFF_CHANGES -> baseline.also {
                it.language = context.language
                it.additionArguments = context.additionArguments
            }
            AnalyserType.GIT -> baseline.also {
                it.language = context.language
                it.repoId = context.repo
                it.additionArguments = context.additionArguments
            }
            else -> null
        }
    }
}

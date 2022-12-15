package org.archguard.scanner.ctl.command

import org.archguard.scanner.core.AnalyserSpec
import org.archguard.scanner.core.client.ArchGuardClient
import org.archguard.scanner.core.context.AnalyserType
import org.archguard.scanner.ctl.client.ArchGuardConsoleClient
import org.archguard.scanner.ctl.client.ArchGuardCsvClient
import org.archguard.scanner.ctl.client.ArchGuardHttpClient
import org.archguard.scanner.ctl.client.ArchGuardJsonClient
import org.archguard.scanner.ctl.client.ChainedArchGuardClient
import org.archguard.scanner.ctl.impl.OfficialAnalyserSpecs

/**
 * this class is the 'controller' of the scanner
 * put all input parameters in and build the context out
 **/
data class ScannerCommand(
    val type: AnalyserType = AnalyserType.SOURCE_CODE,
    val systemId: String,
    val serverUrl: String,
    val path: String,
    val output: List<String> = emptyList(),
    val customizedAnalyserSpecs: List<AnalyserSpec> = emptyList(),

    // for source code analysing, may be Map, String or AnalyserSpec
    val language: String? = null,
    val features: List<String> = emptyList(),

    // for git analysing
    val repoId: String? = null,
    val branch: String = "master",
    val startedAt: Long = 0,
    // for diff changes analysing
    val since: String? = null,
    val until: String? = null,
    val depth: Int = 7,

    /**
     * slot is for plugins after analyser
     * relate to [org.archguard.meta.Slot]
     **/
    val slots: List<AnalyserSpec> = emptyList(),
) {
    private val allAnalyserSpecs = customizedAnalyserSpecs + slots + OfficialAnalyserSpecs.specs()
    fun getAnalyserSpec(identifier: String) = allAnalyserSpecs.find { it.identifier == identifier.lowercase() }
        ?: throw IllegalArgumentException("No analyser found for identifier: $identifier")

    fun buildClient(): ArchGuardClient {
        val clients = mutableListOf<ArchGuardClient>()

        if (output.contains("http")) {
            clients.add(ArchGuardHttpClient(language ?: "", serverUrl, systemId, path, this))
        }

        if (output.contains("json")) {
            clients.add(ArchGuardJsonClient(systemId))
        }

        if (output.contains("csv")) {
            clients.add(ArchGuardCsvClient(systemId))
        }

        if (output.contains("console") || output.isEmpty()) {
            clients.add(ArchGuardConsoleClient(systemId))
        }

        return ChainedArchGuardClient(clients)
    }

    companion object
}

package org.archguard.scanner.core.architecture

import org.archguard.scanner.core.client.ArchGuardClient
import org.archguard.scanner.core.context.AnalyserType
import org.archguard.scanner.core.context.Context

interface ArchitectureContext : Context {
    override val type: AnalyserType get() = AnalyserType.ARCHITECTURE

    val path: String
    val language: String
}

class CliArchitectureContext(
    override val path: String,
    override val language: String,
    override val client: ArchGuardClient
) : ArchitectureContext
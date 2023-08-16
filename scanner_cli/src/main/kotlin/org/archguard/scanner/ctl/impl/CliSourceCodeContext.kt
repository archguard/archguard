package org.archguard.scanner.ctl.impl

import org.archguard.scanner.core.client.ArchGuardClient
import org.archguard.scanner.core.sourcecode.SourceCodeContext

data class CliSourceCodeContext(
    override val language: String,
    override val features: List<String>,
    override val client: ArchGuardClient,
    override val path: String,
    override val withFunctionCode: Boolean,
    override val debug: Boolean,
) : SourceCodeContext

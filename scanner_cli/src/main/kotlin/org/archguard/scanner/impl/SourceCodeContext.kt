package org.archguard.scanner.impl

import org.archguard.scanner.core.context.ArchGuardClient
import org.archguard.scanner.core.context.Context

class SourceCodeContext(
    override val language: String,
    override val features: List<String>,
    override val client: ArchGuardClient,
    val path: String,
    val systemId: String,
    val withoutStorage: Boolean,
) : Context

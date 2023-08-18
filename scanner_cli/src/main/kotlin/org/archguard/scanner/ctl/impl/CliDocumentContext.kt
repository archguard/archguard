package org.archguard.scanner.ctl.impl

import org.archguard.scanner.core.client.ArchGuardClient
import org.archguard.scanner.core.document.DocumentContext

data class CliDocumentContext(
    override val client: ArchGuardClient,
    override val path: String,
    override val branch: String
) : DocumentContext

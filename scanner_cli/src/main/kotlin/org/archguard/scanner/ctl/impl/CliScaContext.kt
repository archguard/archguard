package org.archguard.scanner.ctl.impl

import org.archguard.scanner.core.client.ArchGuardClient
import org.archguard.scanner.core.sca.ScaContext

data class CliScaContext(
    override val client: ArchGuardClient,
    override val path: String,
    override val language: String,
) : ScaContext

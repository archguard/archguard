package org.archguard.scanner.ctl.impl

import org.archguard.scanner.core.client.ArchGuardClient
import org.archguard.scanner.core.estimate.EstimateContext

data class CliEstimateContext(
    override val client: ArchGuardClient,
    override val path: String,
    override val branch: String,
) : EstimateContext

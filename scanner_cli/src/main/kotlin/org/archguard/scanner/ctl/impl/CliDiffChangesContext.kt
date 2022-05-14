package org.archguard.scanner.ctl.impl

import org.archguard.scanner.core.client.ArchGuardClient
import org.archguard.scanner.core.diffchanges.DiffChangesContext

data class CliDiffChangesContext(
    override val client: ArchGuardClient,
    override val path: String,
    override val branch: String,
    override val since: String,
    override val until: String,
    override val depth: Int,
) : DiffChangesContext

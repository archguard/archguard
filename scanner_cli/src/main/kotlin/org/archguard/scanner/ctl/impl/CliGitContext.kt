package org.archguard.scanner.ctl.impl

import org.archguard.scanner.core.client.ArchGuardClient
import org.archguard.scanner.core.git.GitContext

data class CliGitContext(
    override val client: ArchGuardClient,
    override val path: String,
    override val repoId: String,
    override val branch: String,
    override val startedAt: Long,
) : GitContext

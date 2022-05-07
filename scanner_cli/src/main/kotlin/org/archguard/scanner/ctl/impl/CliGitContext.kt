package org.archguard.scanner.ctl.impl

import org.archguard.scanner.core.client.ArchGuardClient
import org.archguard.scanner.core.git.GitContext

// extend SourceCodeContext to accept cil specific parameters(infra related), like memory limit, queue size, etc.
data class CliGitContext(
    override val client: ArchGuardClient,
    override val path: String,
    override val repoId: String,
    override val branch: String,
    override val startedAt: Long,
    val systemId: String,
) : GitContext

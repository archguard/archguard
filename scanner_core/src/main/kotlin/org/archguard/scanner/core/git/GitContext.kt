package org.archguard.scanner.core.git

import org.archguard.scanner.core.context.AnalyserType
import org.archguard.scanner.core.context.Context

interface GitContext : Context {
    override val type: AnalyserType get() = AnalyserType.GIT

    val path: String
    val repoId: String
    val branch: String
    val startedAt: Long
}

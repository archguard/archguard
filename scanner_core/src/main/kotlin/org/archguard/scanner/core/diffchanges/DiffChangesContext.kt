package org.archguard.scanner.core.diffchanges

import org.archguard.scanner.core.context.AnalyserType
import org.archguard.scanner.core.context.Context

interface DiffChangesContext : Context {
    override val type: AnalyserType get() = AnalyserType.DIFF_CHANGES

    val path: String
    val branch: String
    val since: String
    val until: String
    val depth: Int
}

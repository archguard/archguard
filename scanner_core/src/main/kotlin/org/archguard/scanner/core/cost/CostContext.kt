package org.archguard.scanner.core.cost

import org.archguard.scanner.core.context.Context

interface CostContext: Context {
    val path: String
    val repoId: String
    val branch: String
}

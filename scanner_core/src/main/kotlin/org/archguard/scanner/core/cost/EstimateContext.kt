package org.archguard.scanner.core.cost

import org.archguard.scanner.core.context.Context

interface EstimateContext: Context {
    val path: String
    val repoId: String
    val branch: String
}

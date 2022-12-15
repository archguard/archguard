package org.archguard.scanner.cost

import org.archguard.scanner.core.client.ArchGuardClient
import org.archguard.scanner.core.client.EmptyArchGuardClient
import org.archguard.scanner.core.context.AnalyserType
import org.archguard.scanner.core.cost.CostContext
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

class SimpleCostContext(
     override val type: AnalyserType = AnalyserType.COST,
     override val client: ArchGuardClient = EmptyArchGuardClient(),
     override val path: String = ".",
     override val repoId: String = "",
     override val branch: String = "",
) : CostContext

class CostServiceTest {
    @Test
    @Disabled
    fun analyse() {
        val context = SimpleCostContext(
            path = ".",
            repoId = "test",
            branch = "master"
        )

        val estimates = CostService(context).analyse()
        println("estimates: $estimates")
    }
}
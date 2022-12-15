package org.archguard.scanner.analyser

import org.archguard.scanner.core.client.ArchGuardClient
import org.archguard.scanner.core.client.EmptyArchGuardClient
import org.archguard.scanner.core.context.AnalyserType
import org.archguard.scanner.core.estimate.EstimateContext
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

class SimpleEstimateContext(
     override val type: AnalyserType = AnalyserType.ESTIMATE,
     override val client: ArchGuardClient = EmptyArchGuardClient(),
     override val path: String = ".",
     override val branch: String = "",
) : EstimateContext

class EstimateServiceTest {
    @Test
    @Disabled
    fun analyse() {
        val context = SimpleEstimateContext(
            path = ".",
            branch = "master"
        )

        val estimates = EstimateService(context).analyse()
        println("estimates: $estimates")
    }
}
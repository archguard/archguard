package org.archguard.scanner.cost.estimate

import CocomoEstimate
import io.kotest.matchers.doubles.shouldBeBetween
import org.junit.jupiter.api.Test

internal class CocomoEstimateTest {
    @Test
    fun basic_cost_estimate() {
        val estimate = CocomoEstimate.effort(26, 1.0)
        val cost = CocomoEstimate.cost(estimate, 56000, 2.4)

        cost.shouldBeBetween(580.0, 585.0, 0.0)
    }

    @Test
    fun many_lines_cost_estimate() {
        val estimate = CocomoEstimate.effort(77873, 1.0)
        val cost = CocomoEstimate.cost(estimate, 56000, 2.4)

        cost.shouldBeBetween(2602000.0, 2602100.0, 0.0)
    }

    @Test
    fun schedule_months_estimate() {
        val estimate = CocomoEstimate.effort(537, 1.0)
        val months = CocomoEstimate.months(estimate)

        months.shouldBeBetween(2.6, 2.8, 0.0)
    }
}
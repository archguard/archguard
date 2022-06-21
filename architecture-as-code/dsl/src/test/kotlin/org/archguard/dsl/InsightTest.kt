package org.archguard.dsl

import org.archguard.dsl.insight.insight
import org.junit.jupiter.api.Test

internal class InsightTest {
    @Test
    internal fun basic_insight() {
        val insight = insight {
            name("demo")
            comparison(">=")
            version("1.2.3")
            field("version")
        }
    }
}

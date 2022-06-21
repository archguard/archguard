package org.archguard.dsl

import org.archguard.dsl.insight.insight
import org.archguard.dsl.insight.regexp
import org.junit.jupiter.api.Test

internal class InsightTest {
    @Test
    internal fun basic_insight() {
        val insight = insight {
            name("demo")
            condition(">= 1.2.3")
            field("version") {
                regexp("org.apache.logging.log4j")
            }
        }

        println(insight)
    }
}

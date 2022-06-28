package org.archguard.dsl

import org.archguard.dsl.insight.insight
import org.junit.jupiter.api.Test

internal class InsightTest {
    @Test
    internal fun basic_insight_with_regex() {
        val insight = insight {
            name("demo")
            field("version")
            condition(">= 1.2.3")
        }
    }

    @Test
    internal fun basic_insight() {
        val insight = insight {
            name("demo")
            field("version", "org.apache.logging.log4j")

            condition(">= 1.2.3")
        }
    }
}

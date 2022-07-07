package org.archguard.domain.insight

import org.junit.jupiter.api.Test

internal class InsightsParserTest {

    @Test
    internal fun sample() {
        val result = InsightsParser().tokenize("field:method != 'sample'")
        println()
    }
}
package org.archguard.domain.insight

import org.junit.jupiter.api.Test

internal class InsightsParserTest {

    @Test
    internal fun sample() {
        InsightsParser().tokenize("field:method != 'sample'")
    }
}
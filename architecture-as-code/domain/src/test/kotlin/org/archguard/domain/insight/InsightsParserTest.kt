package org.archguard.domain.insight

import org.junit.jupiter.api.Test

internal class InsightsParserTest {

    @Test
    fun sample() {
        val query = InsightsParser("method != 'sample' and file = %world% or suffix != %gif%").parse()
        val queryString = query.toString()
        println()
    }
}
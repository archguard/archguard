package org.archguard.scanner.core.context;

import org.archguard.rule.core.Rule
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test


class AnalyserTypeTest {

    @Test
    fun `given valid type string, when fromString is called, then return corresponding AnalyserType`() {
        // given
        val typeString = "source_code"

        // when
        val analyserType = AnalyserType.fromString(typeString)

        // then
        assertEquals(AnalyserType.SOURCE_CODE, analyserType)
    }

    @Test()
    fun `given invalid type string, when fromString is called, then throw IllegalArgumentException`() {
        // given
        val typeString = "invalid_type"

        // when
        assertThrows(NullPointerException::class.java) {
            AnalyserType.fromString(typeString)!!
        }
    }
}

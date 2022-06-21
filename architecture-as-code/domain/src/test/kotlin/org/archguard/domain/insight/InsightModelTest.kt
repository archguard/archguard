package org.archguard.domain.insight

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class InsightModelTest {
    @Test
    internal fun should_return_error_when_no_parse() {
//        InsightModel.parse("field:version >= 12.3")
        val model = InsightModel.parse("field:version || 'sample'")
        assertEquals(null, model)
    }
}
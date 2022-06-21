package org.archguard.domain.insight

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class InsightModelTest {
    @Test
    internal fun should_return_error_when_no_parse() {
        val model = InsightModel.parse("field:version || 'sample'")
        assertEquals(null, model)
    }

    @Test
    internal fun normal_parse() {
        val model = InsightModel.parse("field:version == 'sample'")!!
        assertEquals("version", model.field)
        assertEquals("==", model.valueExpr.comparison)
        assertEquals("'sample'", model.valueExpr.value)
    }
}

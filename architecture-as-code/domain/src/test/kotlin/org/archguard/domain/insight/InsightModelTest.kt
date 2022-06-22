package org.archguard.domain.insight

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class InsightModelTest {
    @Test
    internal fun should_return_error_when_no_parse() {
        val models = InsightModel.parse("field:version || 'sample'")
        assertEquals(0, models.size)
    }

    @Test
    internal fun normal_parse() {
        val model = InsightModel.parse("field:version == 'sample'")[0]
        assertEquals("version", model.field)
        assertEquals("==", model.valueExpr.comparison)
        assertEquals("'sample'", model.valueExpr.value)
    }

    @Test
    internal fun multiple_field() {
        val models =
            InsightModel.parse("field:version == \"1.2.3\" field:artifact == 'kotlin-logging' field:group == /.*logback/ ")
        assertEquals(3, models.size)

        assertEquals("\"1.2.3\"", models[0].valueExpr.value)
        assertEquals("1.2.3", models[0].fieldFilter.value)

        assertEquals("'kotlin-logging'", models[1].valueExpr.value)
        assertEquals("kotlin-logging", models[1].fieldFilter.value)

        assertEquals("/.*logback/", models[2].valueExpr.value)
        assertEquals(InsightFilterType.REGEXP, models[2].fieldFilter.type)
        assertEquals(".*logback", models[2].fieldFilter.value)
    }
}

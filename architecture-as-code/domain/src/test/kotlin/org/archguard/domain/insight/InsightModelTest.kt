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
    internal fun sca_normal_parse() {
        val model = InsightModel.parse("field:version == 'sample'")[0]
        assertEquals("version", model.field)
        assertEquals("==", model.valueExpr.comparison)
        assertEquals("'sample'", model.valueExpr.value)
    }

    @Test
    internal fun sca_multiple_field() {
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

    @Test
    internal fun api_multiple_field() {
        val models =
            InsightModel.parse("field:method == 'get' field:package == 'com.thoughtworks.archguard'")
        assertEquals(2, models.size)

        assertEquals("'get'", models[0].valueExpr.value)
        assertEquals("'com.thoughtworks.archguard'", models[1].valueExpr.value)
    }

    @Test
    internal fun like_support() {
        val models = InsightModel.parse("field:method == %get%")
        assertEquals(1, models.size)

        assertEquals("%get%", models[0].fieldFilter.value)
        assertEquals(InsightFilterType.LIKE, models[0].fieldFilter.type)
    }

    @Test
    internal fun like_in_string() {
        val models = InsightModel.parse("field:method == 'get%'")
        assertEquals(1, models.size)

        assertEquals("get%", models[0].fieldFilter.value)
        assertEquals(InsightFilterType.LIKE, models[0].fieldFilter.type)
    }

    @Test
    internal fun to_sql_query() {
        val models = InsightModel.parse("field:method == %get%")
        assertEquals("method like '%get%'", InsightModel.toQuery(models))

        val multiples = InsightModel.parse("field:method == %get% field:name == %test%")
        assertEquals("method like '%get%' and name like '%test%'", InsightModel.toQuery(multiples))
    }
}

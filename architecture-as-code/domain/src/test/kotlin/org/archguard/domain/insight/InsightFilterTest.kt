package org.archguard.domain.insight

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class InsightFilterTest {
    @Test
    internal fun should_return_error_when_no_parse() {
        val models = InsightFilter.parse("field:version || 'sample'")
        assertEquals(0, models.size)
    }

    @Test
    internal fun sca_normal_parse() {
        val model = InsightFilter.parse("field:version == 'sample'")[0]
        assertEquals("version", model.field)
        assertEquals("==", model.valueExpr.symbol)
        assertEquals("'sample'", model.valueExpr.value)
    }

    @Test
    internal fun sca_multiple_field() {
        val models =
            InsightFilter.parse("field:version == \"1.2.3\" field:artifact == 'kotlin-logging' field:group == /.*logback/ ")
        assertEquals(3, models.size)

        assertEquals("\"1.2.3\"", models[0].valueExpr.value)
        assertEquals("1.2.3", models[0].filter.value)

        assertEquals("'kotlin-logging'", models[1].valueExpr.value)
        assertEquals("kotlin-logging", models[1].filter.value)

        assertEquals("/.*logback/", models[2].valueExpr.value)
        assertEquals(InsightFilterType.REGEXP, models[2].filter.type)
        assertEquals(".*logback", models[2].filter.value)
    }

    @Test
    internal fun api_multiple_field() {
        val models =
            InsightFilter.parse("field:method == 'get' field:package == 'com.thoughtworks.archguard'")
        assertEquals(2, models.size)

        assertEquals("'get'", models[0].valueExpr.value)
        assertEquals("'com.thoughtworks.archguard'", models[1].valueExpr.value)
    }

    @Test
    internal fun like_support() {
        val models = InsightFilter.parse("field:method == %get%")
        assertEquals(1, models.size)

        assertEquals("%get%", models[0].filter.value)
        assertEquals(InsightFilterType.LIKE, models[0].filter.type)
    }

    @Test
    internal fun like_in_string() {
        val models = InsightFilter.parse("field:method == 'get%'")
        assertEquals(1, models.size)

        assertEquals("get%", models[0].filter.value)
        assertEquals(InsightFilterType.LIKE, models[0].filter.type)
    }

    @Test
    internal fun to_sql_query() {
        val models = InsightFilter.parse("field:method == %get%")
        assertEquals("method like '%get%'", InsightFilter.toQuery(models))

        val multiples = InsightFilter.parse("field:method == %get% field:name == %test%")
        assertEquals("method like '%get%' and name like '%test%'", InsightFilter.toQuery(multiples))
    }

    @Test
    internal fun not_equal() {
        val models = InsightFilter.parse("field:method != 'sample'")
        assertEquals("method != 'sample'", InsightFilter.toQuery(models))

        val equalModels = InsightFilter.parse("field:method == 'sample'")
        assertEquals("method = 'sample'", InsightFilter.toQuery(equalModels))
    }
}

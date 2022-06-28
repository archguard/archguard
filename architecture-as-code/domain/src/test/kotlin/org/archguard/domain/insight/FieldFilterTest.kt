package org.archguard.domain.insight

import org.archguard.domain.comparison.Comparison
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class FieldFilterTest {
    @Test
    internal fun should_return_error_when_no_parse() {
        val models = FieldFilter.parse("field:version || 'sample'")
        assertEquals(0, models.size)
    }

    @Test
    internal fun sca_normal_parse() {
        val model = FieldFilter.parse("field:version == 'sample'")[0]
        assertEquals("version", model.name)
        assertEquals(Comparison.Equal, model.comparison)
        assertEquals("sample", model.value)
    }

    @Test
    internal fun sca_multiple_field() {
        val models =
            FieldFilter.parse("field:version == \"1.2.3\" field:artifact == 'kotlin-logging' field:group == /.*logback/ ")
        assertEquals(3, models.size)

        assertEquals("1.2.3", models[0].value)

        assertEquals("kotlin-logging", models[1].value)

        assertEquals(".*logback", models[2].value)
        assertEquals(FilterType.REGEXP, models[2].type)
    }

    @Test
    internal fun api_multiple_field() {
        val models =
            FieldFilter.parse("field:method == 'get' field:package == 'com.thoughtworks.archguard'")
        assertEquals(2, models.size)

        assertEquals("get", models[0].value)
        assertEquals("com.thoughtworks.archguard", models[1].value)
    }

    @Test
    internal fun like_support() {
        val models = FieldFilter.parse("field:method == %get%")
        assertEquals(1, models.size)

        assertEquals("%get%", models[0].value)
        assertEquals(FilterType.LIKE, models[0].type)
    }

    @Test
    internal fun like_in_string() {
        val models = FieldFilter.parse("field:method == 'get%'")
        assertEquals(1, models.size)

        assertEquals("get%", models[0].value)
        assertEquals(FilterType.LIKE, models[0].type)
    }

    @Test
    internal fun to_sql_query() {
        val models = FieldFilter.parse("field:method == %get%")
        assertEquals("method like '%get%'", FieldFilter.toQuery(models))

        val multiples = FieldFilter.parse("field:method == %get% field:name == %test%")
        assertEquals("method like '%get%' and name like '%test%'", FieldFilter.toQuery(multiples))
    }

    @Test
    internal fun not_equal() {
        val models = FieldFilter.parse("field:method != 'sample'")
        assertEquals("method != 'sample'", FieldFilter.toQuery(models))

        val equalModels = FieldFilter.parse("field:method == 'sample'")
        assertEquals("method = 'sample'", FieldFilter.toQuery(equalModels))
    }
}

package org.archguard.scanner.sourcecode.xml.mybatis

import org.apache.ibatis.builder.ParameterExpression
import org.apache.ibatis.parsing.GenericTokenParser
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class MyBatisHandlerTest {
    @Test
    internal fun should_parse_parameters() {
        var key = ""
        val parser = GenericTokenParser("#{", "}") { content: String ->
            key = content
            "#{$content}"
        }

        val parse = parser.parse("#{item.orderId}")

        assertEquals("item.orderId", key)
    }

    @Test
    internal fun should_handle_parameter_exp() {
        val parameterExpression = ParameterExpression("item.createTime,jdbcType=TIMESTAMP")
        assertEquals(2, parameterExpression.size)
    }

    @Test
    internal fun dynamic_sql_source() {

    }
}
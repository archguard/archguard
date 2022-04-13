package org.archguard.scanner.sourcecode.xml.mybatis

import org.apache.ibatis.builder.ParameterExpression
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class MyBatisHandlerTest {
    @Test
    internal fun should_handle_parameter_exp() {
        val parameterExpression = ParameterExpression("item.createTime,jdbcType=TIMESTAMP")
        assertEquals(2, parameterExpression.size)
    }
}
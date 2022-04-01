package org.archguard.ident.mysql

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class MysqlIdentAppTest {
    @Test
    internal fun should_ident_table_name_in_query_annotation() {
        val sql =
            "select source_package as sourcePackage, source_class as sourceClass, source_method as sourceMethod," +
                    " target_url as targetUrl, target_http_method as targetHttpMethod, system_id as systemId from " +
                    "container_demand where system_id = :systemId"

        val relation = MysqlIdentApp.analysis(sql)

        assertEquals("container_demand", relation!!.tableNames.joinToString(","))
    }

    @Test
    internal fun should_ident_table_name_in_create_query() {
        val sql =
            "select a.clzname aClz, b.clzname bClz from code_method a, code_method b, code_ref_method_callees mc " +
                    "where a.id = mc.a and b.id = mc.b and a.module='${'$'}module' and b.module='${'$'}module' and" +
                    " a.system_id='${'$'}systemId' and b.system_id='${'$'}systemId' and mc.system_id='${'$'}systemId'"

        val relation = MysqlIdentApp.analysis(sql)

        assertEquals("code_method,code_ref_method_callees", relation!!.tableNames.joinToString(","))
    }

    @Test
    internal fun should_handle_exception() {
        val code = "select id, system_name as systemName,\"+\" language from system_info where id in (<ids>)"
        val relation = MysqlIdentApp.analysis(code)

        assertEquals(null, relation)
    }

    @Test
    internal fun should_handle_table_name_in_backtick() {
        val code = "select id, system_name as systemName,\"+\" language from `system_info` where id in (:ids)"
        val relation = MysqlIdentApp.analysis(code)

        assertEquals("system_info", relation!!.tableNames.joinToString(","))
    }

    @Test
    internal fun should_handle_replaced_ids() {
        val code = "select id, system_name as systemName,\"+\" language from system_info where id in (:ids)"
        val relation = MysqlIdentApp.analysis(code)

        assertEquals("system_info", relation!!.tableNames.joinToString(","))
    }
}
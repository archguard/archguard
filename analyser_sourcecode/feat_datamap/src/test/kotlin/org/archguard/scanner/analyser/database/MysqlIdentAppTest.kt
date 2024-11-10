package org.archguard.scanner.analyser.database

import org.junit.jupiter.api.Assertions.assertEquals
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

        assertEquals("system_info", relation!!.tableNames.joinToString(","))
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

    @Test
    internal fun should_parse_for_mybatis_questions() {
        val code =
            "INSERT INTO oms_order_operate_history (order_id, operate_man, create_time, order_status, note) VALUES (?, ?, ?, ?, ?)"
        val relation = MysqlIdentApp.analysis(code)

        assertEquals("oms_order_operate_history", relation!!.tableNames.joinToString(","))
    }

    @Test
    internal fun handle_for_update_with_tabel_name() {
        val code = "update sms_coupon_product_category_relation"
        val relation = MysqlIdentApp.analysis(code)

        assertEquals("sms_coupon_product_category_relation", relation!!.tableNames.joinToString(","))
    }

    @Test
    internal fun utf8_character() {
        val code = "select * from `表名`"
        val relation = MysqlIdentApp.analysis(code)

        assertEquals("表名", relation!!.tableNames.joinToString(","))
    }

    @Test
    internal fun select_from_issues() {
        val code = "select ' from table"
        val relation = MysqlIdentApp.analysis(code)

        assertEquals("table", relation!!.tableNames.joinToString(","))
    }

    @Test
    internal fun select_from_substring_end_error() {
        val code =
            "SELECT COUNT(*) FROM vip_price_config_v2 WHERE platform = ? AND month = ? AND sub_type = ? AND suit_type = ? %s;\", \""
        val relation = MysqlIdentApp.analysis(code)

        assertEquals("vip_price_config_v2", relation!!.tableNames.joinToString(","))
    }

    @Test
    internal fun select_from_substring_start_error() {
        val code = "\"INSERT INTO playlist_archive_0 (pid,aid,sort,`desc`) VALUES (?,?,?,?)\", plArcHit(pid)"
        val relation = MysqlIdentApp.analysis(code)

        assertEquals("playlist_archive_0", relation!!.tableNames.joinToString(","))
    }

//    @Test
//    internal fun parse_sql() {
//        val stmt = CCJSqlParserUtil.parseStatements("SELECT * FROM tab1; SELECT * FROM tab2")
//        println(stmt.statements)
//    }
}

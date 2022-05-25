package org.archguard.scanner.analyser.database

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class MysqlAnalyserTest {
    @Test
    fun should_wrapper_in_list_in_values() {
        val sqlify =
            MysqlAnalyser().sqlify("select id, system_name as systemName, language from system_info where id in (<ids>)")

        assertEquals("select id, system_name as systemName, language from system_info where id in (:ids)", sqlify)
    }

    @Test
    fun should_wrapper_raw_string_in_values() {
        val sqlify =
            MysqlAnalyser().sqlify(
                "\"\"\"\n" +
                    "                select count(m.id) from method_access m inner join code_method c where m.method_id = c.id  \n" +
                    "                and m.system_id = :systemId and m.is_static=1 and m.is_private=0 \n" +
                    "                and c.name not in ('<clinit>', 'main') and c.name not like '%\$%'\n" +
                    "            \"\"\".trimIndent()"
            )

        assertEquals(false, sqlify.contains("trimIndent"))
        assertEquals(false, sqlify.contains("\"\"\""))
    }

    @Test
    fun should_handle_variable_in_sql() {
        val sqlify = MysqlAnalyser().sqlify("select id, module_name from \"\\\"+orderSqlPiece+\"\\\"")
        assertEquals("select id, module_name from *", sqlify)
    }

    @Test
    fun should_handle_plus_without_double_quote() {
        val sqlify = MysqlAnalyser().sqlify("select id, system_name as systemName, language from system_info +\"")
        assertEquals("select id, system_name as systemName, language from system_info ", sqlify)
    }

    @Test
    fun should_kotlin_variable_in_sql() {
        val sqlify = MysqlAnalyser().sqlify("select id, module_name from system and c.name = '${'$'}name'")
        assertEquals("select id, module_name from system and c.name = ''", sqlify)
    }

    @Test
    fun should_kotlin_variable_in_sql_without_quote() {
        val sqlify = MysqlAnalyser().sqlify("select id, module_name from system and c.name = ${'$'}name")
        assertEquals("select id, module_name from system and c.name = ''", sqlify)
    }

    @Test
    fun should_handle_end_with_string_append() {
        val sqlify = MysqlAnalyser().sqlify("select id, module_name from system and c.name = ${'$'}name\"+moduleFilter")
        assertEquals("select id, module_name from system and c.name = ''", sqlify)
    }

    @Test
    fun should_handle_kotlin_string_in_sql() {
        val sqlify =
            MysqlAnalyser().sqlify("SELECT id, name, module, loc, access FROM code_class where system_id = '' and name = '' '' limit ''")
        assertEquals(
            "SELECT id, name, module, loc, access FROM code_class where system_id = '' and name = '' limit 10",
            sqlify
        )
    }
}

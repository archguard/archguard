package org.archguard.scanner.sourcecode.database

import chapi.app.analyser.KotlinAnalyserApp
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.nio.file.Paths
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
            MysqlAnalyser().sqlify("\"\"\"\n" +
                    "                select count(m.id) from method_access m inner join code_method c where m.method_id = c.id  \n" +
                    "                and m.system_id = :systemId and m.is_static=1 and m.is_private=0 \n" +
                    "                and c.name not in ('<clinit>', 'main') and c.name not like '%\$%'\n" +
                    "            \"\"\".trimIndent()")

        assertEquals(false, sqlify.contains("trimIndent"))
        assertEquals(false, sqlify.contains("\"\"\""))
    }

    @Test
    fun should_handle_variable_in_sql() {
        val sqlify =
            MysqlAnalyser().sqlify("select id, module_name from \"\\\"+orderSqlPiece+\"\\\"\"")

        assertEquals("select id, module_name from *", sqlify)
    }

    @Test
    fun should_handle_kotlin_string_in_sql() {
        val sqlify = MysqlAnalyser().sqlify("\"select id, name, module, loc, access from code_class where system_id=:systemId and name=:name and module <=> :module\"")

        assertEquals("select id, name, module, loc, access from code_class where system_id='' and name='' and module <=> ''", sqlify)
    }

    @Test
    fun should_ident_jdbi_create_query_annotation() {
        val resource = this.javaClass.classLoader.getResource("jdbi/ContainerServiceDao.kt")!!
        val path = Paths.get(resource.toURI()).toFile().absolutePath

        val nodes = KotlinAnalyserApp().analysisNodeByPath(path)
        val mysqlAnalyser = MysqlAnalyser()

        val logs: List<SqlRecord> = nodes.flatMap {
            mysqlAnalyser.analysisByNode(it, "")
        }

        assertEquals(4, logs.size)
        assertEquals("select source_package as sourcePackage, source_class as sourceClass, source_method as sourceMethod, target_url as targetUrl, target_http_method as targetHttpMethod, system_id as systemId from container_demand where system_id = :systemId", logs[0].Sql[0])
    }

    @Test
    fun should_ident_query_in_code() {
        val resource = this.javaClass.classLoader.getResource("jdbi/PackageRepositoryImpl.kt")!!
        val path = Paths.get(resource.toURI()).toFile().absolutePath

        val nodes = KotlinAnalyserApp().analysisNodeByPath(path)
        val mysqlAnalyser = MysqlAnalyser()

        val sqlRecord: List<SqlRecord> = nodes.flatMap {
            mysqlAnalyser.analysisByNode(it, "")
        }

        assertEquals(2, sqlRecord.size)
        assertEquals("code_method,code_ref_method_callees", sqlRecord[0].Tables.joinToString(","))
        assertEquals("code_ref_class_dependencies", sqlRecord[1].Tables.joinToString(","))
    }

    @Test
    @Disabled
    fun should_ident_in_variable() {
        val resource = this.javaClass.classLoader.getResource("jdbi/TestBadSmellRepositoryImpl.kt")!!
        val path = Paths.get(resource.toURI()).toFile().absolutePath

        val nodes = KotlinAnalyserApp().analysisNodeByPath(path)
        val mysqlAnalyser = MysqlAnalyser()

        val sqlRecord: List<SqlRecord> = nodes.flatMap {
            mysqlAnalyser.analysisByNode(it, "")
        }

        assertEquals(13, sqlRecord.size)
        assertEquals("method_access,code_method", sqlRecord[0].Tables.joinToString(","))
        assertEquals("method_access,code_method", sqlRecord[1].Tables.joinToString(","))
        assertEquals("code_method,code_ref_method_callees", sqlRecord[2].Tables.joinToString(","))
        assertEquals("code_method,code_ref_method_callees", sqlRecord[3].Tables.joinToString(","))
    }
}
package org.archguard.scanner.sourcecode.database

import chapi.app.analyser.KotlinAnalyserApp
import org.junit.jupiter.api.Test
import java.nio.file.Paths
import kotlin.test.assertEquals

internal class MysqlAnalyserTest {
    @Test
    fun should_ident_jdbi_create_query_annotation() {
        val resource = this.javaClass.classLoader.getResource("jdbi/ContainerServiceDao.kt")!!
        val path = Paths.get(resource.toURI()).toFile().absolutePath

        val nodes = KotlinAnalyserApp().analysisNodeByPath(path)
        val mysqlAnalyser = MysqlAnalyser()

        val logs: List<MysqlLog> = nodes.flatMap {
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

        val logs: List<MysqlLog> = nodes.flatMap {
            mysqlAnalyser.analysisByNode(it, "")
        }

        assertEquals(2, logs.size)
    }
}
package org.archguard.ident.mysql

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class MysqlIdentAppTest {
    @Test
    internal fun identForSqlQuery() {
        val relation =
            MysqlIdentApp().analysis("select source_package as sourcePackage, source_class as sourceClass, source_method as sourceMethod, target_url as targetUrl, target_http_method as targetHttpMethod, system_id as systemId from container_demand where system_id = :systemId")

        assertEquals("container_demand", relation.tableNames.joinToString(","))
    }
}
package org.archguard.ident.mysql

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test


internal class MysqlIdentAppTest {
    @Test
    internal fun parseOneTableName() {
        val table = MysqlIdentApp().analysis("SELECT * FROM tab1")
        assertEquals(1, table.tableNames.size)
        assertEquals("tab1", table.tableNames[0])
    }

    @Test
    internal fun parseTwoTableName() {
        val table = MysqlIdentApp().analysis("select a.clzname aClz, b.clzname bClz from JMethod a, JMethod b, _MethodCallees mc " +
                "where a.id = mc.a and b.id = mc.b and a.module=demo and b.module=bmodule and a.system_id=3 and b.system_id=4 and mc.system_id=5")

        assertEquals(2, table.tableNames.size)
        assertEquals("JMethod", table.tableNames[0])
        assertEquals("_MethodCallees", table.tableNames[1])
    }
}
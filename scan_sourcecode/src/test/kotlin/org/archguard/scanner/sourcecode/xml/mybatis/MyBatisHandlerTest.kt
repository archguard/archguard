package org.archguard.scanner.sourcecode.xml.mybatis

import org.junit.jupiter.api.Test
import java.io.FileInputStream
import kotlin.io.path.toPath
import kotlin.test.assertEquals

internal class MyBatisHandlerTest {
    @Test
    internal fun dynamic_sql_source() {
        val resource = this.javaClass.classLoader.getResource("mybatis/OmsOrderOperateHistoryDao.xml")!!
        val toURI = resource.toURI().toPath().toAbsolutePath()
        val sqls = MyBatisHandler().streamToSqls(FileInputStream(toURI.toString()))
        assertEquals("INSERT INTO oms_order_operate_history (order_id, operate_man, create_time, order_status, note) VALUES (?, ?, ?, ?, ?)", sqls[0])
    }
}
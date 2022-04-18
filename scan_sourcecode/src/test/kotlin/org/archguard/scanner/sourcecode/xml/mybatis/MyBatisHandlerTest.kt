package org.archguard.scanner.sourcecode.xml.mybatis

import org.junit.jupiter.api.Test
import java.io.FileInputStream
import kotlin.io.path.toPath
import kotlin.test.assertEquals

internal class MyBatisHandlerTest {
    @Test
    internal fun support_for_foreach() {
        val resource = this.javaClass.classLoader.getResource("mybatis/OmsOrderOperateHistoryDao.xml")!!
        val toURI = resource.toURI().toPath().toAbsolutePath()
        val sqls = MyBatisHandler().streamToSqls(toURI.toString())
        assertEquals(
            "INSERT INTO oms_order_operate_history (order_id, operate_man, create_time, order_status, note) VALUES (?, ?, ?, ?, ?)",
            sqls.methodSqlMap["insertList"]
        )
    }

    @Test
    internal fun official_author_map_test_cases() {
        val resource = this.javaClass.classLoader.getResource("mybatis/AuthorMapper.xml")!!
        val toURI = resource.toURI().toPath().toAbsolutePath()
        val sqls = MyBatisHandler().streamToSqls(toURI.toString())

        assertEquals(15, sqls.methodSqlMap.size)
    }

    @Test
    internal fun official_blog_map_test_cases() {
        val resource = this.javaClass.classLoader.getResource("mybatis/BlogMapper.xml")!!
        val toURI = resource.toURI().toPath().toAbsolutePath()
        val sqls = MyBatisHandler().streamToSqls(toURI.toString())

        assertEquals(6, sqls.methodSqlMap.size)
    }

    @Test
    internal fun include_in_mapper() {
        val resource = this.javaClass.classLoader.getResource("mybatis/PmsBrandMapper.xml")!!
        val toURI = resource.toURI().toPath().toAbsolutePath()
        val sqls = MyBatisHandler().streamToSqls(toURI.toString())

        assertEquals(14, sqls.methodSqlMap.size)
    }

    @Test
    internal fun should_handle_refid_lost() {
        val resource = this.javaClass.classLoader.getResource("mybatis/OrderMapper.xml")!!
        val toURI = resource.toURI().toPath().toAbsolutePath()
        val sqls = MyBatisHandler().streamToSqls(toURI.toString())

        assertEquals(1, sqls.methodSqlMap.size)
    }
}
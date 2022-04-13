package org.archguard.scanner.sourcecode.xml

import org.junit.jupiter.api.Test
import kotlin.io.path.toPath
import kotlin.test.assertEquals

internal class XmlParserTest {
    @Test
    internal fun should_handle_mybatis() {
        val resource = this.javaClass.classLoader.getResource("mybatis/OmsOrderOperateHistoryDao.xml")!!
        val mybatisEntry = XmlParser.fromFile(resource.toURI().toPath().toString())?.parseMyBatis()!!
        assertEquals("com.macro.mall.dao.OmsOrderOperateHistoryDao", mybatisEntry.namespace)
    }

    @Test
    internal fun parser_from_path() {
        val resource = this.javaClass.classLoader.getResource("mybatis")!!
        val entries = XmlParser.fromPath(resource.toURI().toPath().toString())
        assertEquals(3, entries.size)
    }
}
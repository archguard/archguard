package org.archguard.scanner.sourcecode.xml

import org.junit.jupiter.api.Test
import kotlin.io.path.toPath
import kotlin.test.assertEquals

internal class XmlParserTest {
    @Test
    internal fun should_handle_mybatis() {
        val resource = this.javaClass.classLoader.getResource("mybatis/OmsOrderOperateHistoryDao.xml")!!
        val mybatisEntry = XmlParser.fromPath(resource.toURI().toPath())?.parseMyBatis()!!
        assertEquals("com.macro.mall.dao.OmsOrderOperateHistoryDao", mybatisEntry.namespace)
    }
}
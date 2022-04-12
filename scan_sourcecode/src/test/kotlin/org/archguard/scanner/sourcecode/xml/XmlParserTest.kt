package org.archguard.scanner.sourcecode.xml

import org.junit.jupiter.api.Test
import kotlin.io.path.toPath

internal class XmlParserTest {
    @Test
    internal fun should_handle_mybatis() {
        val resource = this.javaClass.classLoader.getResource("mybatis/OmsOrderOperateHistoryDao.xml")!!
        XmlParser.fromPath(resource.toURI().toPath())
    }
}
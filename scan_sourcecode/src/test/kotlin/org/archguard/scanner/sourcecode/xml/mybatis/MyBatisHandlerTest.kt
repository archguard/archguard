package org.archguard.scanner.sourcecode.xml.mybatis

import org.junit.jupiter.api.Test
import kotlin.io.path.toPath

internal class MyBatisHandlerTest {
    @Test
    internal fun dynamic_sql_source() {
        val resource = this.javaClass.classLoader.getResource("mybatis/OmsOrderOperateHistoryDao.xml")!!
        val toURI = resource.toURI().toPath().toAbsolutePath()
        MyBatisHandler().compute(toURI.toString())
    }
}
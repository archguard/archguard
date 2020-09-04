package com.thoughtworks.archguard.report.infrastructure

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.web.WebAppConfiguration

@SpringBootTest
@WebAppConfiguration
internal class CodeLineRepositoryImplTest {
    @Autowired
    lateinit var codeLineRepositoryImpl: SizingRepositoryImpl

    /**
     *   H2 似乎不支持日期比较。。。
     */
//    @Test
//    @Sql("classpath:sqls/insert_method_statistic.sql")
//    internal fun should_get_method_line_when_page() {
//        val systemId = 6L
//        val threshold = 30
//        val limit = 2L
//        val offset = 2L
//        val page = codeLineRepositoryImpl.getMethodLinesAboveThreshold(systemId, threshold, limit, offset)
//        assertThat(page.size).isEqualTo(2)
//        assertThat(page[0].lines).isEqualTo(62)
//        assertThat(page[1].lines).isEqualTo(56)
//    }
}
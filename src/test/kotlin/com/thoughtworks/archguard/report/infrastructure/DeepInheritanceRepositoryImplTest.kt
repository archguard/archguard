package com.thoughtworks.archguard.report.infrastructure

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.web.WebAppConfiguration
import kotlin.test.assertEquals

@SpringBootTest
@WebAppConfiguration
internal class DeepInheritanceRepositoryImplTest {

    @Autowired
    lateinit var deepInheritanceRepositoryImpl: DeepInheritanceRepositoryImpl

    @Test
    @Sql("classpath:sqls/insert_jclass_and_lcom4_dit.sql")
    fun should_get_data_clumps_total_count() {
        val lcoM4AboveThresholdCount = deepInheritanceRepositoryImpl.getDitAboveThresholdCount(1, 1)
        assertEquals(2, lcoM4AboveThresholdCount)
    }

    @Test
    @Sql("classpath:sqls/insert_jclass_and_lcom4_dit.sql")
    fun should_get_data_clumps_list_by_paging() {

        val classDitExceedThreshold = deepInheritanceRepositoryImpl
                .getDitAboveThresholdList(1, 1, 1, 0)
        kotlin.test.assertEquals(1, classDitExceedThreshold.size)
        kotlin.test.assertEquals(3, classDitExceedThreshold.get(0).dit)
        kotlin.test.assertEquals("org.apache.dubbo.demo",
                classDitExceedThreshold.get(0).packageName)
        kotlin.test.assertEquals("DemoService",
                classDitExceedThreshold.get(0).typeName)
        kotlin.test.assertEquals("dubbo-demo-interface",
                classDitExceedThreshold.get(0).moduleName)
    }
}
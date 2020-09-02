package com.thoughtworks.archguard.metrics.infrastructure

import org.junit.jupiter.api.Test

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.web.WebAppConfiguration

@SpringBootTest
@WebAppConfiguration
internal class ClassMetricRepositoryImplTest {

    @Autowired
    lateinit var classMetricRepositoryImpl: ClassMetricRepositoryImpl

    @Test
    @Sql("classpath:sqls/insert_jclass_and_lcom4_dit.sql")
    fun should_get_one_lcom4_class_when_threshold_1_page_1() {
        val page1 = classMetricRepositoryImpl
                .getClassLCOM4ExceedThresholdWithPaging(1, 1, 1, 0)
        kotlin.test.assertEquals(1, page1.size)
        kotlin.test.assertEquals(3, page1.get(0).lcom4Value)
        kotlin.test.assertEquals("org.apache.dubbo.demo.DemoService",
                page1.get(0).jClassVO.name)
        kotlin.test.assertEquals("dubbo-demo-interface",
                page1.get(0).jClassVO.module)

        val page2 = classMetricRepositoryImpl
                .getClassLCOM4ExceedThresholdWithPaging(1, 1, 1, 1)
        kotlin.test.assertEquals(1, page2.size)
        kotlin.test.assertEquals(2, page2.get(0).lcom4Value)
        kotlin.test.assertEquals("org.apache.dubbo.demo.TestService",
                page2.get(0).jClassVO.name)
        kotlin.test.assertEquals("test-demo-interface",
                page2.get(0).jClassVO.module)
    }

    @Test
    @Sql("classpath:sqls/insert_jclass_and_lcom4_dit.sql")
    fun should_get_one_dit_class_when_threshold_is_3() {
        val classDitExceedThreshold = classMetricRepositoryImpl
                .getClassDitExceedThreshold(1, 1, 1, 0)
        kotlin.test.assertEquals(1, classDitExceedThreshold.size)
        kotlin.test.assertEquals(3, classDitExceedThreshold.get(0).ditValue)
        kotlin.test.assertEquals("org.apache.dubbo.demo.DemoService",
                classDitExceedThreshold.get(0).jClassVO.name)
        kotlin.test.assertEquals("dubbo-demo-interface",
                classDitExceedThreshold.get(0).jClassVO.module)
    }
}
package com.thoughtworks.archguard.metrics.infrastructure

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
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
    fun getClassLCOM4ExceedThreshold() {
        val classLCOM4ExceedThreshold = classMetricRepositoryImpl
                .getClassLCOM4ExceedThreshold(1, 1)
        kotlin.test.assertEquals(1, classLCOM4ExceedThreshold.size)
        kotlin.test.assertEquals(3, classLCOM4ExceedThreshold.get(0).lcom4Value)
        kotlin.test.assertEquals("org.apache.dubbo.demo.DemoService",
                classLCOM4ExceedThreshold.get(0).jClassVO.name)
        kotlin.test.assertEquals("dubbo-demo-interface",
                classLCOM4ExceedThreshold.get(0).jClassVO.module)
    }

//    @Test
//    @Sql("classpath:sqls/insert_jclass_and_lcom4_dit.sql")
//    fun getClassDitExceedThreshold() {
//        val classDitExceedThreshold = classMetricRepositoryImpl
//                .getClassDitExceedThreshold(1, 1)
//        kotlin.test.assertEquals(1, classDitExceedThreshold.size)
//        kotlin.test.assertEquals(3, classDitExceedThreshold.get(0).ditValue)
//        kotlin.test.assertEquals("org.apache.dubbo.demo.DemoService",
//                classDitExceedThreshold.get(0).jClassVO.name)
//        kotlin.test.assertEquals("dubbo-demo-interface",
//                classDitExceedThreshold.get(0).jClassVO.module)
//    }
}
package com.thoughtworks.archguard.code.module.infrastructure

import com.thoughtworks.archguard.v2.frontier.clazz.domain.ClazzType
import com.thoughtworks.archguard.v2.frontier.clazz.domain.JClass
import com.thoughtworks.archguard.v2.frontier.clazz.domain.JClassRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.jdbc.Sql
import org.springframework.transaction.annotation.Transactional

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
internal class JClassRepositoryImplTest {
    @Autowired
    lateinit var jClassRepository: JClassRepository

    @Test
    @Sql("classpath:sqls/insert_jclass.sql")
    @Disabled
    internal fun should_get_jclass_by_name() {
        val systemId: Long = 1
        val jClass =
            jClassRepository.getJClassBy(systemId, "org.apache.dubbo.demo.GreetingService", "dubbo-demo-interface")
        val expectedClass = JClass(
            "c1983476-7bd8-4e52-a523-71c4f3f5098e",
            "org.apache.dubbo.demo.GreetingService",
            "dubbo-demo-interface"
        )
        expectedClass.addClassType(ClazzType.INTERFACE)
        assertThat(jClass).isEqualToComparingFieldByField(expectedClass)

        val jClass2 =
            jClassRepository.getJClassBy(systemId, "org.apache.dubbo.demo.DemoService", "dubbo-demo-interface")
        val expectedClass2 =
            JClass("c65ee9c2-dab5-4ebb-8a0f-b8682eddd9d8", "org.apache.dubbo.demo.DemoService", "dubbo-demo-interface")
        assertThat(jClass2).isEqualToComparingFieldByField(expectedClass2)
    }
}

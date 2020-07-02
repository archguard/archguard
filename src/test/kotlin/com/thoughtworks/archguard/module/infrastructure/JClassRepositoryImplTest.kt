package com.thoughtworks.archguard.module.infrastructure

import com.thoughtworks.archguard.module.domain.JClass
import com.thoughtworks.archguard.module.domain.JClassRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.web.WebAppConfiguration

@SpringBootTest
@WebAppConfiguration
internal class JClassRepositoryImplTest {
    @Autowired
    lateinit var jClassRepository: JClassRepository

    @Test
    @Sql("classpath:sqls/insert_jclass.sql")
    internal fun should_get_jclass_by_name() {
        val jClass = jClassRepository.getJClassByName("org.apache.dubbo.demo.GreetingService")
        assertThat(jClass).isEqualToComparingFieldByField(JClass("c1983476-7bd8-4e52-a523-71c4f3f5098e", "org.apache.dubbo.demo.GreetingService", "dubbo-demo-interface"))
    }
}
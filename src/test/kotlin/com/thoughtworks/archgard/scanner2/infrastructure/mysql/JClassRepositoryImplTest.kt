package com.thoughtworks.archgard.scanner2.infrastructure.mysql

import com.thoughtworks.archgard.scanner2.domain.model.JClass
import com.thoughtworks.archgard.scanner2.domain.service.Dependency
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
    lateinit var jClassRepositoryImpl: JClassRepositoryImpl

    @Test
    @Sql("classpath:sqls/insert_jclass_and_class_dependencies.sql")
    fun should_get_all_class_dependencies_which_has_modules() {
        val classDependencies = jClassRepositoryImpl.getAllClassDependencies(8L)
        assertThat(classDependencies).containsExactlyElementsOf(listOf(Dependency("000d417d-9482-4fe4-9411-6df3816d8828", "0b463b79-a8dd-4df7-8dc9-3eee8737c1ad")))
    }

    @Test
    @Sql("classpath:sqls/insert_jclass_and_class_dependencies.sql")
    fun should_find_classes_by_systemId_className_and_moduleName() {
        val jClass = jClassRepositoryImpl.findClassBy(8L, "org.springframework.messaging.simp.stomp.StompCommand", "spring-messaging")
        assertThat(jClass).isEqualTo(JClass("0b463b79-a8dd-4df7-8dc9-3eee8737c1ad", "org.springframework.messaging.simp.stomp.StompCommand", "spring-messaging"))
    }
}
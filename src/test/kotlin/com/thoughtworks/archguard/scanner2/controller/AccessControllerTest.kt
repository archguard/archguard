package com.thoughtworks.archguard.scanner2.controller

import com.thoughtworks.archguard.scanner2.domain.model.ClassAccess
import com.thoughtworks.archguard.scanner2.domain.model.MethodAccess
import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.Jdbi
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.jdbc.Sql
import org.springframework.transaction.annotation.Transactional
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Transactional
@Disabled
internal class AccessControllerTest(@Autowired val accessController: AccessController,
                                    @Autowired val jdbi: Jdbi) {

    @Test
    @Sql("classpath:sqls/insert_jclass_and_class_dependencies.sql")
    fun should_save_code_class_access() {
        accessController.persist(8)
        val classResult = jdbi.withHandle<List<ClassAccess>, RuntimeException> { handle: Handle ->
            handle.createQuery("select class_id as id, is_interface, is_abstract, is_synthetic from code_class_access order by class_id")
                    .mapTo(ClassAccess::class.java).list()
        }.toList()
        assertEquals(2, classResult.size)
        assertTrue(classResult[0].isInterface)
        assertFalse(classResult[0].isSynthetic)
        assertTrue(classResult[0].isAbstract)

        val methodResult = jdbi.withHandle<List<MethodAccess>, RuntimeException> { handle: Handle ->
            handle.createQuery("select method_id as id, is_synthetic, is_abstract, is_static, is_private from method_access order by method_id")
                    .mapTo(MethodAccess::class.java).list()
        }.toList()
        assertEquals(2, methodResult.size)

        //4164: 01000001000100
        assertTrue(methodResult[0].isSynthetic)
        assertFalse(methodResult[0].isAbstract)
        assertFalse(methodResult[0].isPrivate)
        assertFalse(methodResult[0].isStatic)

        //4106: 01000000001010
        assertTrue(methodResult[1].isSynthetic)
        assertFalse(methodResult[1].isAbstract)
        assertTrue(methodResult[1].isStatic)
        assertTrue(methodResult[1].isPrivate)

    }
}
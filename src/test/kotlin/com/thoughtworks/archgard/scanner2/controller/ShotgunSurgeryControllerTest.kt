package com.thoughtworks.archgard.scanner2.controller

import com.thoughtworks.archgard.scanner2.domain.model.CognitiveComplexity
import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.Jdbi
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.jdbc.Sql
import kotlin.test.assertEquals

@SpringBootTest
@ActiveProfiles("test")
internal class ShotgunSurgeryControllerTest(@Autowired val shotgunSurgeryController: ShotgunSurgeryController,
                                            @Autowired val jdbi: Jdbi) {

    @Test
    @Sql("classpath:sqls/insert_commit_log_and_change_log.sql")
    fun should_save_cognitive_complexity() {
        shotgunSurgeryController.persist(0)
        val result = jdbi.withHandle<List<CognitiveComplexity>, RuntimeException> { handle: Handle ->
            handle.createQuery("select * from cognitive_complexity")
                    .mapTo(CognitiveComplexity::class.java).list()
        }
        val arrayList = ArrayList(result)
        assertEquals(5, arrayList.size)
    }
}
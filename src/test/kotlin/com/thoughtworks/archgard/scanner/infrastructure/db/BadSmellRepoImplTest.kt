package com.thoughtworks.archgard.scanner.infrastructure.db

import com.thoughtworks.archgard.scanner.domain.bs.BadSmell
import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.Jdbi
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
internal class BadSmellRepoImplTest(@Autowired val badSmellRepoImpl: BadSmellRepoImpl, @Autowired val jdbi: Jdbi) {

    @Test
    fun save() {
        badSmellRepoImpl.save(listOf(
                BadSmell("1", "enity_name", 11, "description", 3, "longMethod"),
                BadSmell("2", "enity_name1", 13, "description1", 43, "longMethod")))

        val count = jdbi.withHandle<Int, RuntimeException> { handle: Handle ->
            handle.createQuery("select count(*) from badSmell")
                    .mapTo(Int::class.java).one()
        }
        assertEquals(count, 2)
    }
}
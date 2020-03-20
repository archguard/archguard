package com.thoughtworks.archguard.report.infrastructure

import org.jdbi.v3.core.Jdbi
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
class BadSmellRepo(@Autowired private val jdbi: Jdbi) {

    fun getBadSmellCount(): List<BadSmellCountDBO> {
        return jdbi.withHandle<List<BadSmellCountDBO>, Nothing> { handle ->
            handle.createQuery("select count(id),type from badSmell group by type")
                    .mapTo(BadSmellCountDBO::class.java)
                    .list()
        }
    }


}
package com.thoughtworks.archguard.report_bak.infrastructure

import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.mapper.reflect.ConstructorMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
class BadSmellRepo(@Autowired private val jdbi: Jdbi) {

    fun getBadSmellCount(): List<BadSmellCountDBO> {
        return jdbi.withHandle<List<BadSmellCountDBO>, Nothing> { handle ->
            handle.registerRowMapper(ConstructorMapper.factory(BadSmellCountDBO::class.java))
            handle.createQuery("select type, count(id) as size from badSmell group by type")
                    .mapTo(BadSmellCountDBO::class.java)
                    .list()
        }
    }


}
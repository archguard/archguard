package com.thoughtworks.archguard.scanner.infrastructure.db

import org.jdbi.v3.core.Jdbi
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
class PmdRepository(@Autowired val jdbi: Jdbi) {

    fun save(vs: List<Violation>) {
        jdbi.useHandle<Exception> {
            val insert = "insert into violation(system_id,`file`,beginline,endline,priority,`text`) values (?,?,?,?,?,?)"
            vs.forEach { v ->
                it.execute(insert, v.systemId, v.file, v.beginline, v.endline, v.priority, v.text)
            }
        }
    }


    fun clean() {
        jdbi.useHandle<Exception> {
            val delete = "delete from violation where 1=1"
            it.execute(delete)
        }
    }

}

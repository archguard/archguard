package com.thoughtworks.archgard.scanner.domain.scanner.pmd

import org.jdbi.v3.core.Jdbi
import org.springframework.beans.factory.annotation.Autowired

class PmdRepository(@Autowired val jdbi: Jdbi) {

    fun save(vs: List<Violation>) {
        jdbi.useHandle<Exception> {
            val insert = "insert into violation(`file`,beginline,endline,priority,`text`) values (?,?,?,?,?)"
            vs.forEach { v ->
                it.execute(insert, v.file, v.beginline, v.endline, v.priority, v.text)
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

package com.thoughtworks.archguard.report.infrastructure

import org.jdbi.v3.core.Jdbi
import org.springframework.beans.factory.annotation.Autowired

class HotSpotRepo(@Autowired private val jdbi: Jdbi) {

    fun queryHotSpotPath(top: Int): List<Pair<String, Int>> {
        return jdbi.withHandle<List<Pair<String, Int>>, Exception> {
            val sql = """
                select count(new_path) as n,new_path 
                from change_entry 
                where chng_mode<>'DELETE' 
                group by new_path  
                having n>1
                order by n desc 
                """.trimIndent()
            it.createQuery(sql).setMaxRows(top)
                    .map<Pair<String, Int>> { rs, _ ->
                        Pair(rs.getString("new_path"), rs.getInt("n"))
                    }.list()
        }
    }

}

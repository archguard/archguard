package com.thoughtworks.archguard.report_bak.infrastructure

import org.jdbi.v3.core.Jdbi
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
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

    fun queryLatestHotSpotPath(number: Int): List<String> {
        return jdbi.withHandle<List<String>, Exception> {
            val sql = """
                select change_entry.new_path as new_path,
                commit_log.commit_time as commit_time
                from commit_log, change_entry 
                where change_entry.cmt_id = commit_log.id 
                    and (new_path like '%.java' or new_path like '%.kt')
                    and new_path not like '%test%'
                    and change_entry.chng_mode <> 'DELETE'
                order by commit_time
                """.trimIndent()
            it.createQuery(sql).setMaxRows(number)
                    .map { rs, _ ->
                        rs.getString("new_path")
                    }.list()
        }
    }

    fun queryLatestHotSpotTest(number: Int): List<String> {
        return jdbi.withHandle<List<String>, Exception> {
            val sql = """
                select change_entry.new_path as new_path,
                commit_log.commit_time as commit_time
                from commit_log, change_entry 
                where change_entry.cmt_id = commit_log.id 
                    and (new_path like '%.java' or new_path like '%.kotlin')
                    and new_path like '%test%'
                    and change_entry.chng_mode <> 'DELETE'
                order by commit_time
                """.trimIndent()
            it.createQuery(sql).setMaxRows(number)
                    .map { rs, _ ->
                        rs.getString("new_path")
                    }.list()
        }
    }

}

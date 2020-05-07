package com.thoughtworks.archguard.report.infrastructure

import com.thoughtworks.archguard.report.domain.model.ChangeEntry
import com.thoughtworks.archguard.report.domain.model.CommitLog
import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.mapper.RowMapper
import org.jdbi.v3.core.result.ResultIterable
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
class ScatteredRepo(@Autowired private val jdbi: Jdbi) {
    val commitRowMapper = RowMapper { rs, _ ->
        CommitLog(
                id = rs.getString("id"),
                commitTime = rs.getLong("commit_time"),
                shortMessage = rs.getString("short_msg"),
                committer_name = rs.getString("cmttr_name"),
                repId = rs.getLong("rep_id"),
                chgdEntryCnt = rs.getInt("chgd_entry_cnt"),
                entries = entriesIn(rs.getString("id"))
        )
    }

    fun findScatteredCommits(time: Long, changedEntryCount: Int): List<CommitLog> {
        return jdbi.withHandle<List<CommitLog>, Exception> {
            val sql = """
|                   select id, commit_time, short_msg, cmttr_name, rep_id , chgd_entry_cnt
|                   from commit_log
|                   where chgd_entry_cnt>=:chgd_entry_cnt and commit_time>:time
|                   order by commit_time desc
|                   """.trimMargin()
            it.createQuery(sql)
                    .bind("chgd_entry_cnt", changedEntryCount)
                    .bind("time", time)
                    .map(commitRowMapper)
                    .toList()
        }
    }

    fun findAllCommitLogs(time: Long): List<CommitLog> {
        return jdbi.withHandle<List<CommitLog>, Exception> {
            val sql = """
|                   select id, commit_time, short_msg, cmttr_name, rep_id , chgd_entry_cnt
|                   from commit_log
|                   where commit_time>:time
|                   order by commit_time desc
|                   """.trimMargin()
            it.createQuery(sql)
                    .bind("time", time)
                    .map(commitRowMapper)
                    .toList()
        }
    }

    fun appendChangedEntriesQuantityToCommitLog(): Int {
        var count = 0
        jdbi.useHandle<Exception> {
            val commitLogOfNeedUpdate = "select id,commit_time from commit_log where chgd_entry_cnt is null"
            it.createQuery(commitLogOfNeedUpdate)
                    .mapToMap()
                    .onEach { commit ->
                        inlinePrvsStateOfEachEntryIn(commit)
                    }.onEach { commit ->
                        inlineCountToCommit(commit)
                        count++
                    }
        }
        return count
    }

    /*将复杂度变化的文件数量保存到 commit 记录中*/
    private fun inlineCountToCommit(commit: Map<String, Any>) {
        jdbi.useHandle<Exception> {
            val count = "select count(*) from change_entry where cgntv_cmplxty<>prvs_cgn_cmplxty and cmt_id=:cmt_id"
            val update = "update commit_log set chgd_entry_cnt=($count) where id=:cmt_id"
            it.createUpdate(update)
                    .bind("cmt_id", commit["id"])
                    .execute()
        }
    }


    /*遍历当前提交的所有 change entry */
    private fun inlinePrvsStateOfEachEntryIn(commit: Map<String, Any>) {
        fun entriesInCurrrentCommit(commit: Map<String, Any>, handle: Handle): ResultIterable<MutableMap<String, Any>> {
            val modifiedJavaEntry = "select new_path,cmt_id from change_entry where cmt_id=:cmt_id and new_path like '%.java' and chng_mode='MODIFY'"
            return handle.createQuery(modifiedJavaEntry)
                    .bind("cmt_id", commit["id"])
                    .mapToMap()
        }

        jdbi.useHandle<Exception> {
            entriesInCurrrentCommit(commit, it)
                    .forEach { entry ->
                        inlineEntryStateInPrvsCmt(entry, commit)
                    }
        }
    }

    /*获得 changeEntry 在上一次提交的复杂度和提交ID */
    private fun inlineEntryStateInPrvsCmt(entry: Map<String, Any>, commit: Map<String, Any>) {
        fun getPrvs(entry: Map<String, Any>, commit: Map<String, Any>): Map<String, Any> {
            return jdbi.withHandle<Map<String, Any>, Exception> {
                val prvsData = """
                        select prvs.cmt_id, prvs.cgntv_cmplxty
                        from change_entry prvs join commit_log c on prvs.cmt_id=c.id 
                        where prvs.new_path=:new_path and c.commit_time<=:commit_time and prvs.cmt_id<>:cmt_id
                        order by c.commit_time desc
                        limit 1 
                        """.trimIndent()
                it.createQuery(prvsData)
                        .bind("new_path", entry["new_path"])
                        .bind("commit_time", commit["commit_time"])
                        .bind("cmt_id", entry["cmt_id"])
                        .mapToMap()
                        .findOne()
                        .orElse(mapOf(Pair("cmt_id", "no previous record in db"), Pair("cgntv_cmplxty", -1)))
            }
        }

        inlinePrvsData(getPrvs(entry, commit), entry)
    }

    /*使用上一次提交的复杂度和提交ID 更新 changeEntry 的两个字段*/
    private fun inlinePrvsData(prvs: Map<String, Any>, entry: Map<String, Any>) {
        jdbi.useHandle<Exception> {
            val sql = """
                update change_entry 
                set prvs_cmt_id=:prvs_cmt_id, prvs_cgn_cmplxty=:prvs_cgn_cmplxty
                where new_path=:new_path and cmt_id=:cmt_id
                """.trimIndent()
            it.createUpdate(sql)
                    .bind("prvs_cmt_id", prvs["cmt_id"])
                    .bind("prvs_cgn_cmplxty", prvs["cgntv_cmplxty"])
                    .bind("new_path", entry["new_path"])
                    .bind("cmt_id", entry["cmt_id"])
                    .execute()
        }
    }


    private fun entriesIn(commitId: String): Set<ChangeEntry> {
        return jdbi.withHandle<Set<ChangeEntry>, Exception> {

            val changeEntryMapper = RowMapper { rs, _ ->
                ChangeEntry(
                        newPath = rs.getString("new_path"),
                        cognitiveComplexity = rs.getInt("cgntv_cmplxty"),
                        mode = rs.getString("chng_mode"),
                        prvsCmtId = rs.getString("prvs_cmt_id"),
                        prvsCgnCmplxty = rs.getInt("prvs_cgn_cmplxty")
                )
            }

            val changedEntry = """
                    select new_path , cgntv_cmplxty, chng_mode, prvs_cmt_id, prvs_cgn_cmplxty 
                    from change_entry 
                    where cmt_id = ? and cgntv_cmplxty<>prvs_cgn_cmplxty  and prvs_cgn_cmplxty<>-1
                    """.trimIndent()
            it.createQuery(changedEntry)
                    .bind(0, commitId)
                    .map(changeEntryMapper)
                    .toSet()
        }
    }


}
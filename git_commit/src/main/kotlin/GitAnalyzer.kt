package com.thoughtworks.archguard.git.analyzer

import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.mapper.RowMapper
import org.jdbi.v3.core.result.ResultIterable
import org.jdbi.v3.spring4.JdbiFactoryBean
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct


interface GitAnalyzer {

    fun findScatterCommits(): List<CommitLog>
}

@Component
class GitAnalyzerByJdbi(@Autowired val jdbiFactoryBean: JdbiFactoryBean) : GitAnalyzer {
    private lateinit var jdbi: Jdbi
    val logger: Logger = LoggerFactory.getLogger(GitAnalyzerByJdbi::class.java)

    @PostConstruct
    fun jdbi() {
        jdbi = jdbiFactoryBean.`object`
    }

    override fun findScatterCommits(): List<CommitLog> {
        return jdbi.withHandle<List<CommitLog>, Exception> {
            val queryCommit = "select id, commit_time, shortMessage, committer_name, rep_id from CommitLog"
            val resultIterable: ResultIterable<CommitLog> = it.createQuery(queryCommit).map(commitRowMapper())

            resultIterable.filter { commitLog ->
                isScatterCommit(commitLog)
            }.toList()
        }
    }

    private fun commitRowMapper(): RowMapper<CommitLog> {
        return RowMapper { rs, ctx ->
            val commitId = rs.getString("id")
            val entriesSet: Set<ChangeEntry> = entriesIn(commitId)

            CommitLog(
                    id = commitId,
                    commit_time = rs.getLong("commit_time"),
                    shortMessage = rs.getString("shortMessage"),
                    committer_name = rs.getString("committer_name"),
                    rep_id = rs.getLong("rep_id"),
                    entries = entriesSet
            )
        }
    }

    /*判断一个提交是否符合符合霰弹提交的条件*/
    private fun isScatterCommit(commitLog: CommitLog): Boolean {
        var count = 0 // 复杂度变化的文件数量
        val standard = 2 // 复杂度变化的文件数量的标准，达到这个值则视为霰弹提交
        logger.info("评估本次递交：{}", commitLog.id)
        entriesIn(commitLog.id).filter { changeEntry ->
            changeEntry.new_path.endsWith(".java") && changeEntry.mode == ("MODIFY") // java file
        }.forEach { changeEntry ->
            val pre = previousCommit(changeEntry.new_path, commitLog)
            if (changeEntry.cognitiveComplexity != pre["cognitiveComplexity"]) {
                logger.info("{}上一次提交的复杂度{},本次{}", changeEntry.new_path, pre, changeEntry.cognitiveComplexity)

                if (++count == standard) {
                    logger.info("以达到条件+++++++++++++")
                    return true
                }
            }
        }
        return false
    }

    //    上一次提交的文件复杂度
    private fun previousCommit(path: String, commitLog: CommitLog): Map<String, Any> {
        val sql = """
            select c.id, e.cognitiveComplexity 
            from  CommitLog c join ChangeEntry e on c.id=commit_id 
            where  e.new_path=? and c.commit_time<=? and c.id<>? order by c.commit_time desc
            """.trimIndent()
        return jdbi.withHandle<Map<String, Any>, Exception> { handle ->
            handle.createQuery(sql)
                    .bind(0, path)
                    .bind(1, commitLog.commit_time)
                    .bind(2, commitLog.id).mapToMap().first()
        }
    }

    //    todo : SQL 查询中的表明要常量化
//    todo: 字段名字映射
    /*查询一个 Commit 中涉及的所有文件*/
    private fun entriesIn(commitId: String): Set<ChangeEntry> {
        return jdbi.withHandle<Set<ChangeEntry>, Exception> {

            val changeEntryMapper = RowMapper { rs, ctx ->
                ChangeEntry(
                        new_path = rs.getString("new_path"),
                        cognitiveComplexity = rs.getInt("cognitiveComplexity"),
                        mode = rs.getString("mode")
                )
            }

            val queryChangeEntry = "select new_path , cognitiveComplexity, mode from ChangeEntry where commit_id = ?"
            it.createQuery(queryChangeEntry)
                    .bind(0, commitId)
                    .map(changeEntryMapper)
                    .toSet()
        }
    }

}
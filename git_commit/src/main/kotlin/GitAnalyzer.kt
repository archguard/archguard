package com.thoughtworks.archguard.git.analyzer

import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.mapper.RowMapper
import org.jdbi.v3.spring4.JdbiFactoryBean
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.stream.Collectors
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
            val resultIterable = it.createQuery(queryCommit).map(commitRowMapper())
            resultIterable.withStream<List<CommitLog>, Exception> { stream ->
                stream.filter { revCommit ->
//                  过滤掉复杂度没有变化的 commit
                    isScatterCommit(revCommit)
                }.collect(Collectors.toList())
            }
        }
    }

    private fun commitRowMapper(): RowMapper<CommitLog> {
        return RowMapper { rs, ctx ->
            val commitId = rs.getString("id")
            val entriesSet: Set<ChangeEntry> = selectChangeEntry(commitId)

            CommitLog(
                    id = commitId,
                    commit_time = rs.getInt("commit_time"),
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
        selectChangeEntry(commitLog.id).filter { changeEntry ->
            changeEntry.new_path.endsWith(".java") && changeEntry.mode.equals("MODIFY") // java file
        }.forEach { changeEntry ->
            logger.info("本次提交{},msg={}", commitLog.id, commitLog.shortMessage)
            val pre = previousCommitComplexity(changeEntry.new_path, commitLog.commit_time)
            if (changeEntry.cognitiveComplexity != pre) {
                if (++count == standard)
                    return true
            }
        }
        return false
    }

    //    上一次提交的文件复杂度
    private fun previousCommitComplexity(path: String, commitTime: Int): Int {
        val sql = """
            select e.cognitiveComplexity 
            from  CommitLog c join ChangeEntry e on c.id=commit_id 
            where  e.new_path=? and c.commit_time<? order by c.commit_time desc
            """.trimIndent()
        logger.info("查询{}上一次提交的复杂度", path)
        return jdbi.withHandle<Int, Exception> { handle ->
            handle.createQuery(sql).bind(0, path).bind(1, commitTime).mapTo(Int::class.java).first()
        }
    }

    //    todo : SQL 查询中的表明要常量化
//    todo: 字段名字映射
    /*查询一个 Commit 中涉及的所有文件*/
    private fun selectChangeEntry(commitId: String): Set<ChangeEntry> {
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
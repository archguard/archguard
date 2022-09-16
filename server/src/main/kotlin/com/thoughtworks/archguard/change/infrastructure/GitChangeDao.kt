package com.thoughtworks.archguard.change.infrastructure

import com.thoughtworks.archguard.change.domain.model.GitHotFile
import com.thoughtworks.archguard.change.domain.model.GitPathChangeCount
import org.jdbi.v3.sqlobject.statement.SqlQuery

interface GitChangeDao {
    @SqlQuery("select * from scm_git_hot_file where system_id = :systemId")
    fun findBySystemId(systemId: Long): List<GitHotFile>

    @SqlQuery(
        "select system_id as systemId, line_count as lineCount, path, changes" +
            " from scm_path_change_count where system_id = :systemId"
    )
    fun findCountBySystemId(systemId: Long): List<GitPathChangeCount>

    @SqlQuery(
        "select system_id as systemId, line_count as lineCount, path, changes from scm_path_change_count where system_id = :systemId" +
            " order by line_count desc limit :size "
    )
    fun getTopLinesFile(systemId: Long, size: Long): List<GitPathChangeCount>

    @SqlQuery(
        "select system_id as systemId, line_count as lineCount, path, changes from scm_path_change_count where system_id = :systemId" +
            " order by changes desc limit :size "
    )
    fun getTopChangesFile(systemId: Long, size: Long): List<GitPathChangeCount>

    @SqlQuery("select id from scm_commit_log where system_id = :systemId and commit_time between :startTime AND :endTime order by commit_time")
    fun findChangesByRange(systemId: Long, startTime: String, endTime: String): List<String>
}

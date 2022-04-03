package com.thoughtworks.archguard.change.infrastructure

import com.thoughtworks.archguard.change.domain.GitHotFile
import com.thoughtworks.archguard.change.domain.GitPathChangeCount
import org.jdbi.v3.sqlobject.statement.SqlQuery

interface GitHotFileDao {
    @SqlQuery("select * from scm_git_hot_file where system_id = :systemId")
    fun findBySystemId(systemId: Long) : List<GitHotFile>

    @SqlQuery("select system_id as systemId, line_count as lineCount, path, changes" +
            " from scm_path_change_count where system_id = :systemId")
    fun findCountBySystemId(systemId: Long) : List<GitPathChangeCount>

    @SqlQuery("select system_id as systemId, line_count as lineCount, path, changes from scm_path_change_count where system_id = :systemId" +
            " order by line_count desc limit 50 ")
    fun getTopLinesFile(systemId: Long) : List<GitPathChangeCount>

    @SqlQuery("select system_id as systemId, line_count as lineCount, path, changes from scm_path_change_count where system_id = :systemId" +
            " order by changes desc limit 50 ")
    fun getTopChangesFile(systemId: Long) : List<GitPathChangeCount>
}

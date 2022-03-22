package com.thoughtworks.archguard.report.infrastructure

import com.thoughtworks.archguard.report.domain.githotfile.GitHotFile
import com.thoughtworks.archguard.report.domain.githotfile.GitPathChangeCount
import org.jdbi.v3.sqlobject.statement.SqlQuery

interface GitHotFileDao {
    @SqlQuery("select * from scm_git_hot_file where system_id = :systemId")
    fun findBySystemId(systemId: Long) : List<GitHotFile>

    @SqlQuery("select system_id as systemId, path, changes from scm_path_change_count where system_id = :systemId")
    fun findCountBySystemId(systemId: Long) : List<GitPathChangeCount>
}

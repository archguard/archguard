package com.thoughtworks.archguard.report.infrastructure

import com.thoughtworks.archguard.report.domain.githotfile.GitHotFile
import org.jdbi.v3.sqlobject.statement.SqlQuery

interface GitHotFileDao {
    @SqlQuery("select * from git_hot_file where system_id = :systemId")
    fun findBySystemId(systemId: Long) : List<GitHotFile>
}

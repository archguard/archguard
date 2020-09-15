package com.thoughtworks.archgard.scanner.infrastructure.db

import com.thoughtworks.archgard.scanner.domain.scanner.git.GitHotFile
import org.jdbi.v3.sqlobject.customizer.BindBean
import org.jdbi.v3.sqlobject.statement.SqlBatch
import org.jdbi.v3.sqlobject.statement.SqlQuery

interface GitHotFileDao {
    @SqlBatch("insert into git_hot_file (system_id, name, modified_count) " +
            "values (:gitHotFile.systemId, :gitHotFile.name, :gitHotFile.modifiedCount)")
    fun saveAll(@BindBean("gitHotFile") gitHotFiles: List<GitHotFile>)
    
    @SqlQuery("select * from git_hot_file where system_id = :systemId")
    fun findBySystemId(systemId: Long) : List<GitHotFile>
}

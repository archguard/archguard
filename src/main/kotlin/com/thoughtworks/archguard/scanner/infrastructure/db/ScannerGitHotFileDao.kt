package com.thoughtworks.archguard.scanner.infrastructure.db

import com.thoughtworks.archguard.scanner.domain.scanner.git.GitHotFile
import org.jdbi.v3.sqlobject.customizer.BindBean
import org.jdbi.v3.sqlobject.statement.SqlBatch
import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.jdbi.v3.sqlobject.statement.SqlUpdate

interface ScannerGitHotFileDao {
    @SqlBatch("insert into git_hot_file (system_id, repo, path, module_name, class_name, modified_count, jclass_id) " +
            "values (:gitHotFile.systemId, :gitHotFile.repo, :gitHotFile.path, :gitHotFile.moduleName, :gitHotFile.className, :gitHotFile.modifiedCount, :gitHotFile.jclassId)")
    fun saveAll(@BindBean("gitHotFile") gitHotFile: List<GitHotFile>)
    
    @SqlQuery("select * from git_hot_file where system_id = :systemId")
    fun findBySystemId(systemId: Long) : List<GitHotFile>
    
    @SqlUpdate("delete from git_hot_file where system_id = :systemId")
    fun deleteBySystemId(systemId: Long)
}

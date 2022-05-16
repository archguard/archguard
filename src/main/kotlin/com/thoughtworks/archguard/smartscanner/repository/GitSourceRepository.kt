package com.thoughtworks.archguard.smartscanner.repository

import org.archguard.scanner.core.git.ChangeEntry
import org.archguard.scanner.core.git.CommitLog
import org.archguard.scanner.core.git.GitLogs
import org.archguard.scanner.core.git.PathChangeCount
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.sqlobject.customizer.BindBean
import org.jdbi.v3.sqlobject.statement.SqlBatch
import org.jdbi.v3.sqlobject.statement.SqlUpdate
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository

@Repository
class GitSourceRepository(private val jdbi: Jdbi) {
    private val log = LoggerFactory.getLogger(this.javaClass)
    private val dao: GitSourceDao by lazy { jdbi.onDemand(GitSourceDao::class.java) }

    fun saveGitReport(systemId: Long, input: GitLogs) {
        log.debug("clean up old data for systemId: $systemId")
        dao.deleteCommitLogBySystemId(systemId)
        dao.deleteChangeEntryBySystemId(systemId)
        dao.deletePathChangeCountBySystemId(systemId)

        log.debug("save new data for systemId: $systemId")
        dao.saveAllCommitLog(systemId, input.commitLog)
        dao.saveAllChangeEntry(systemId, input.changeEntry)
        dao.saveAllPathChangeCount(systemId, input.pathChangeCount)

        log.debug("save new data for systemId: $systemId done")
    }
}

interface GitSourceDao {
    @SqlBatch(
        """
        INSERT INTO scm_commit_log (system_id,id,commit_time,short_msg,committer_name,committer_email,repo_id)
        VALUES (:systemId,:item.commitTime,:item.shortMessage,:item.committerName,:item.committerEmail,:item.repositoryId)
        """
    )
    fun saveAllCommitLog(systemId: Long, @BindBean("item") commitLogs: List<CommitLog>)

    @SqlBatch(
        """
        INSERT INTO scm_change_entry (system_id,old_path,new_path,commit_time,cognitive_complexity,change_mode,commit_id,committer,line_added,line_deleted)
        VALUES (:systemId,:item.oldPath,:item.newPath,:item.commitTime,:item.cognitiveComplexity,:item.changeMode,:item.commitId,:item.committer,:item.lineAdded,:item.lineDeleted)
        """
    )
    fun saveAllChangeEntry(systemId: Long, @BindBean("item") changeEntries: List<ChangeEntry>)

    @SqlBatch(
        """
        INSERT INTO scm_path_change_count (system_id,id,path,changes,line_count,language)
        VALUES (:systemId,:item.id,:item.path,:item.changes,:item.lineCount,:item.language)
        """
    )
    fun saveAllPathChangeCount(systemId: Long, @BindBean("item") pathChangeCounts: List<PathChangeCount>)

    @SqlUpdate("DELETE FROM scm_commit_log WHERE system_id = :systemId")
    fun deleteCommitLogBySystemId(systemId: Long)

    @SqlUpdate("DELETE FROM scm_change_entry WHERE system_id = :systemId")
    fun deleteChangeEntryBySystemId(systemId: Long)

    @SqlUpdate("DELETE FROM scm_path_change_count WHERE system_id = :systemId")
    fun deletePathChangeCountBySystemId(systemId: Long)
}

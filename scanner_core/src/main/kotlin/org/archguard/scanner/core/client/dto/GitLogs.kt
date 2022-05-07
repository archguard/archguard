package org.archguard.scanner.core.client.dto

data class GitLogs(
    val commitLog: List<CommitLog>,
    val changeEntry: List<ChangeEntry>,
    val pathChangeCount: List<PathChangeCount>
)

data class CommitLog(
    val id: String,
    val commitTime: Long,
    val shortMessage: String,
    val committerName: String,
    val committerEmail: String,
    val repositoryId: String,
)

data class ChangeEntry(
    val oldPath: String,
    val newPath: String,
    val commitTime: Long,
    val cognitiveComplexity: Int,
    val changeMode: String,
    val commitId: String,
    val committer: String,
    val lineAdded: Int,
    val lineDeleted: Int
)

data class PathChangeCount(
    val id: String,
    val path: String,
    val changes: Int,
    val lineCount: Long,
    val language: String,
)

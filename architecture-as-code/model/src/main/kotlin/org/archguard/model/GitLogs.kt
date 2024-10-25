package org.archguard.model

import kotlinx.serialization.Serializable

@Serializable
data class GitLogs(
    val commitLog: List<CommitLog>,
    val changeEntry: List<ChangeEntry>,
    val pathChangeCount: List<PathChangeCount>
)

@Serializable
data class CommitLog(
    val id: String,
    val commitTime: Long,
    val shortMessage: String,
    val committerName: String,
    val committerEmail: String,
    val repositoryId: String,
)

@Serializable
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

// todo: update branch info
@Serializable
data class GitBranch(
    val name: String,
    val lastModified: String = "",
    val lastRead: String = "",
)

@Serializable
data class PathChangeCount(
    val id: String,
    val path: String,
    val changes: Int,
    val lineCount: Long,
    val language: String,
)

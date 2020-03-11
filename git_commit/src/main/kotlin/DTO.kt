package com.thoughtworks.archguard.git.analyzer

data class CommitLog(
        val id: String,
        val commit_time: Long,
        val shortMessage: String,
        val committer_name: String,
        val rep_id: Long,
        val entries: Set<ChangeEntry>)


data class ChangeEntry(
        val new_path: String,
        val cognitiveComplexity: Int,
        val mode: String)


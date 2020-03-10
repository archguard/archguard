package com.thoughtworks.archguard.git.analyzer

data class RevCommit(
        val id: String,
        val commit_time: Int,
        val committer_name: String,
        val committer_email: String,
        val rep_id: Long,
        val entries: Set<ChangeEntry>)


data class ChangeEntry(
        val new_path: String,
        val cognitiveComplexity: Int,
        val mode: String)


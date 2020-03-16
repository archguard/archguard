package com.thoughtworks.archguard.git.scanner


/*为了生成SQL，和DB 表同结构*/


data class GitRepository(
        val rep_path: String,
        val branch: String,
        val id: Long)

data class RevCommit(
        val id: String,
        val commit_time: Int,
        val committer_name: String,
        val committer_email: String,
        val rep_id: Long)


data class ChangeEntry(
        val old_path: String,
        val new_path: String,
        val cognitiveComplexity: Int,
        val mode: String,
        val commit_id: String)


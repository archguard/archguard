package com.thoughtworks.archguard.git.scanner


/*为了生成SQL，和DB 表同结构*/


data class GitRepository(
        val repositoryPath: String,
        val branch: String,
        val id: Long)

data class Commit(
        val id: String,
        val commit_time: Int,
        val committerName: String,
        val committerEmail: String,
        val repositoryId: Long)


data class ChangeEntry(
        val oldPath: String,
        val newPath: String,
        val mode: String,
        val commit_id: String)


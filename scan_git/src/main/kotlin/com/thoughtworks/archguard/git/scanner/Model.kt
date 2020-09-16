package com.thoughtworks.archguard.git.scanner


/*为了生成SQL，和DB 表同结构*/
@Sql("commit_log")
data class CommitLog(
        @Sql("id") val id: String,
        @Sql("commit_time") val commitTime: Long,
        @Sql("short_msg") val shortMessage: String,
        @Sql("committer_name") val committerName: String,
        @Sql("committer_email") val committerEmail: String,
        @Sql("repo_id") val repositoryId: String,
        @Sql("system_id") val systemId: String
)

@Sql("change_entry")
data class ChangeEntry(
        @Sql("old_path") val oldPath: String,
        @Sql("new_path") val newPath: String,
        @Sql("cognitive_complexity") val cognitiveComplexity: Int,
        @Sql("change_mode") val changeMode: String,
        @Sql("commit_id") val commitId: String)

@Target(AnnotationTarget.CLASS, AnnotationTarget.PROPERTY)
annotation class Sql(val value: String)
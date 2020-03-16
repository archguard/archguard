package com.thoughtworks.archguard.git.scanner


/*为了生成SQL，和DB 表同结构*/

@Sql("git_rep")
data class GitRepository(
        @Sql("rep_path") val repositoryPath: String,
        @Sql("branch") val branch: String,
        @Sql("id") val id: Long) // todo： 使用 remote origin 作为id

@Sql("commit_log")
data class CommitLog(
        @Sql("id") val id: String,
        @Sql("commit_time") val commitTime: Long,
        @Sql("short_msg") val shortMessage: String,
        @Sql("cmttr_name") val committerName: String,
        @Sql("cmttr_email") val committerEmail: String,
        @Sql("rep_id") val repositoryId: Long)

@Sql("change_entry")
data class ChangeEntry(
        @Sql("old_path") val oldPath: String,
        @Sql("new_path") val newPath: String,
        @Sql("cgntv_cmplxty") val cognitiveComplexity: Int,
        @Sql("chng_mode") val changeMode: String,
        @Sql("cmt_id") val commitId: String)

@Target(AnnotationTarget.CLASS, AnnotationTarget.PROPERTY)
annotation class Sql(val value: String)
package com.thoughtworks.archguard.git.scanner


/*为了生成SQL，和DB 表同结构*/

@Sql("git_rep")
data class GitRepository(
        @Sql("rep_path") val rep_path: String,
        @Sql("branch") val branch: String,
        @Sql("id") val id: Long) // todo： 使用 remote origin 作为id

@Sql("commit_log")
data class CommitLog(
        @Sql("id") val id: String,
        @Sql("commit_time") val commit_time: Long,
        @Sql("short_msg") val shortMessage: String,
        @Sql("cmttr_name") val committer_name: String,
        @Sql("cmttr_email") val committer_email: String,
        @Sql("rep_id") val rep_id: Long)

@Sql("change_entry")
data class ChangeEntry(
        @Sql("old_path") val old_path: String,
        @Sql("new_path") val new_path: String,
        @Sql("cgntv_cmplxty") val cognitiveComplexity: Int,
        @Sql("chng_mode") val mode: String,
        @Sql("cmt_id") val commit_id: String)

@Target(AnnotationTarget.CLASS, AnnotationTarget.PROPERTY)
annotation class Sql(val value: String)
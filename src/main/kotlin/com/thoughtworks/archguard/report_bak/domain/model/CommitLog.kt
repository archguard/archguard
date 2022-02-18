package com.thoughtworks.archguard.report_bak.domain.model

data class CommitLog(
        val id: String,
        val commitTime: Long,
        val shortMessage: String,
        val committer_name: String,
        val repId: Long,
        val chgdEntryCnt: Int,
        val entries: Set<ChangeEntry>) {
    constructor() : this("", 1L, "", "", 1L, 1, emptySet())
}
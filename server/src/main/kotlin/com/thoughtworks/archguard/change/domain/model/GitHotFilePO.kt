package com.thoughtworks.archguard.change.domain.model

data class GitHotFilePO(
    val systemId: Long,
    val repo: String,
    val path: String,
    val moduleName: String?,
    val className: String?,
    val modifiedCount: Int,
    val jclassId: String?
)

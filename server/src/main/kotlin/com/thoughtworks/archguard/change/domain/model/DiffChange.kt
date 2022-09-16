package com.thoughtworks.archguard.change.domain.model

class DiffChange(
    val systemId: Long,
    val sinceRev: String,
    val untilRev: String,
    val packageName: String?,
    val className: String?,
    val relations: String,
)

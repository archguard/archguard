package com.thoughtworks.archguard.change.domain.model

data class GitPathChangeCount(val systemId: Long, var path: String, val changes: Int, val lineCount: Int)

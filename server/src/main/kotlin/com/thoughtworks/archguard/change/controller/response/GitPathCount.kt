package com.thoughtworks.archguard.change.controller.response

import com.thoughtworks.archguard.change.domain.model.GitPathChangeCount

class GitPathCount(private val gitPathChangeCount: GitPathChangeCount) {
    val name: String
        get() = "root/" + gitPathChangeCount.path

    val value: Int
        get() = gitPathChangeCount.changes

    val lines: Int
        get() = gitPathChangeCount.lineCount
}

package com.thoughtworks.archguard.issue

interface EvoIssueRepository {
    abstract fun getAll(id: Long): List<EvoIssueModel>
}

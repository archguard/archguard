package com.thoughtworks.archguard.evolution.domain

interface EvoIssueRepository {
    abstract fun getAll(id: Long): List<IssueModel>
}

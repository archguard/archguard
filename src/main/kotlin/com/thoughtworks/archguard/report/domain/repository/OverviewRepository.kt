package com.thoughtworks.archguard.report.domain.repository

import com.thoughtworks.archguard.report.infrastructure.GitCommitDBO

interface OverviewRepository {

    fun getGitCommit(): List<GitCommitDBO>

    fun getCodeLinesCount(): Int
}

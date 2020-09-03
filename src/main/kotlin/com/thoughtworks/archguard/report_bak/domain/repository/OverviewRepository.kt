package com.thoughtworks.archguard.report_bak.domain.repository

import com.thoughtworks.archguard.report_bak.infrastructure.GitCommitDBO

interface OverviewRepository {

    fun getGitCommit(): List<GitCommitDBO>

    fun getCodeLinesCount(): Int
}

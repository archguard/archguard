package com.thoughtworks.archguard.report_bak.domain.service

import com.thoughtworks.archguard.report_bak.domain.model.Overview
import com.thoughtworks.archguard.report_bak.domain.repository.OverviewRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class OverviewService(@Autowired val overviewRepository: OverviewRepository) {
    fun getOverview(): Overview {
        val lines = overviewRepository.getCodeLinesCount()
        val gitCommits = overviewRepository.getGitCommit()

        return Overview(lines, gitCommits.size, gitCommits.groupBy { it.cmttr_email }.size)
    }

}

package com.thoughtworks.archguard.report_bak.domain.service

import com.thoughtworks.archguard.report_bak.domain.model.CommitLog
import com.thoughtworks.archguard.report_bak.infrastructure.ScatteredRepo
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class ScatteredService(@Autowired val scatteredRepo: ScatteredRepo) {

    val logger: Logger = LoggerFactory.getLogger(ScatteredService::class.java)

    fun findScatteredCommits(time: Long, changedEntryCount: Int): List<CommitLog> {
        return scatteredRepo.findScatteredCommits(time, changedEntryCount)
    }

    /**统计一个提交中，认知复杂度发生变化的文件数量，并且将此值记录到提交中，以供查询*/
    fun appendChangedEntriesQuantityToCommitLog() {
        logger.info("将复杂度变更的文件数量记录到 scm_commit_log")
        val count = scatteredRepo.appendChangedEntriesQuantityToCommitLog()
        logger.info("更新了{}条 scm_commit_log 记录", count)
    }
}



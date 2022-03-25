package com.thoughtworks.archguard.report_bak.controller

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import com.thoughtworks.archguard.report_bak.domain.model.ChangeEntry
import com.thoughtworks.archguard.report_bak.domain.model.CommitLog
import com.thoughtworks.archguard.report_bak.domain.service.ScatteredService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.util.*


@RestController
@RequestMapping("/api")
class ScatteredController(@Autowired val service: ScatteredService) {


    @GetMapping("/scatter-commits")
    fun scatterCommits(@RequestParam("from", defaultValue = "0") from: Long,
                       @RequestParam("changedEntryCount", defaultValue = "5") changedEntryCount: Int): ResultOut {
        val outList = service.findScatteredCommits(from, changedEntryCount)
                .map { CommitLogOut(it) }
                .toList()
        return ResultOut(outList)
    }

    @PostMapping("/analyze")
    fun analyze() {
        service.appendChangedEntriesQuantityToCommitLog()
    }

    data class ResultOut(val commitLogOutList: List<CommitLogOut>) {
        private var commitCount: Int = 0

        init {
            commitCount = commitLogOutList.size
        }
    }

    data class CommitLogOut(
            val id: String,
            @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
            @JsonProperty("commitTime")
            val commitTime: Date,
            val shortMessage: String,
            val committer: String,
            val repId: Long,
            val changedEntryCount: Int,
            val entrySet: Set<ChangeEntry>) {


        constructor(commitLog: CommitLog) : this(
                id = commitLog.id,
                commitTime = Date(commitLog.commitTime),
                shortMessage = commitLog.shortMessage,
                committer = commitLog.committer_name,
                repId = commitLog.repId,
                changedEntryCount = commitLog.chgdEntryCnt,
                entrySet = commitLog.entries
        )
    }
}


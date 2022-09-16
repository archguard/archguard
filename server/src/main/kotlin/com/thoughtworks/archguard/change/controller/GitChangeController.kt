package com.thoughtworks.archguard.change.controller

import com.thoughtworks.archguard.change.application.GitChangeApplicationService
import com.thoughtworks.archguard.change.controller.response.GitHotFile
import com.thoughtworks.archguard.change.controller.response.GitPathCount
import com.thoughtworks.archguard.change.domain.model.GitPathChangeCount
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/systems/{systemId}/change")
class GitChangeController(val gitChangeApplicationService: GitChangeApplicationService) {

    @GetMapping("/hot-files")
    fun getGitHotFilesBySystemId(@PathVariable("systemId") systemId: Long): List<GitHotFile> {
        return gitChangeApplicationService.getGitHotFilesBySystemId(systemId).map { GitHotFile(it) }
    }

    @GetMapping("/commit-ids")
    fun getChangesInRange(
        @PathVariable("systemId") systemId: Long,
        @RequestParam(value = "startTime", required = true) startTime: String,
        @RequestParam(value = "endTime", required = true) endTime: String,
    ): List<String> {
        return gitChangeApplicationService.getChangesByRange(systemId, startTime, endTime)
    }

    @GetMapping("/path-change-count")
    fun getChangeCountByPath(@PathVariable("systemId") systemId: Long): List<GitPathCount> {
        return gitChangeApplicationService.getPathChangeCount(systemId).map { GitPathCount(it) }
    }

    @GetMapping("/unstable-file")
    fun getHighFrequencyChangeAndLongLines(
        @PathVariable("systemId") systemId: Long,
        @RequestParam(defaultValue = "50") size: Long,
    ): List<GitPathChangeCount> {
        return gitChangeApplicationService.getUnstableFile(systemId, size)
    }
}


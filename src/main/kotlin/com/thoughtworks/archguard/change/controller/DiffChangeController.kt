package com.thoughtworks.archguard.change.controller

import com.thoughtworks.archguard.change.application.DiffChangeService
import com.thoughtworks.archguard.change.application.GitChangeService
import com.thoughtworks.archguard.change.domain.DiffChange
import com.thoughtworks.archguard.system_info.domain.SystemInfoService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/api/systems/{systemId}/diff")
class DiffChangeController(
    val gitChangeService: GitChangeService,
    val diffChangeService: DiffChangeService,
    val systemInfoService: SystemInfoService,
) {

    @GetMapping("/influence")
    fun influence(
        @PathVariable("systemId") systemId: Long,
        @RequestParam(value = "startTime", required = true) startTime: String,
        @RequestParam(value = "endTime", required = true) endTime: String,
    ) {
        val changes = gitChangeService.getChangesByRange(systemId, startTime, endTime)
        if (changes.size < 2) {
            throw ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR, "entity not found"
            )
        }
        val systemInfo = systemInfoService.getSystemInfo(systemId)
        diffChangeService.execute(systemInfo, changes.first(), changes.last())
    }

    @GetMapping("/influence/commit")
    fun influenceByCommit(
        @PathVariable("systemId") systemId: Long,
        @RequestParam(value = "since", required = true) since: String,
        @RequestParam(value = "until", required = true) until: String,
    ): List<DiffChange> {
        val systemInfo = systemInfoService.getSystemInfo(systemId)
        diffChangeService.execute(systemInfo, since, until)
        return diffChangeService.findBySystemId(systemId)
    }
}

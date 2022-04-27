package com.thoughtworks.archguard.change.controller

import com.thoughtworks.archguard.change.application.DiffChangeService
import com.thoughtworks.archguard.change.domain.DiffChange
import com.thoughtworks.archguard.system_info.domain.SystemInfoService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/systems/{systemId}/diff")
class DiffChangeController(
    val diffChangeService: DiffChangeService,
    val systemInfoService: SystemInfoService,
) {
    @GetMapping("/influence/history")
    fun historyInfluence(@PathVariable("systemId") systemId: Long,): List<DiffChange> {
        return diffChangeService.findBySystemId(systemId)
    }

    @GetMapping("/influence/commit")
    fun influenceByCommit(
        @PathVariable("systemId") systemId: Long,
        @RequestParam(value = "since", required = true) since: String,
        @RequestParam(value = "until", required = true) until: String,
        @RequestParam(defaultValue = "1.6.2") scannerVersion: String,
    ): List<DiffChange> {
        val systemInfo = systemInfoService.getSystemInfo(systemId)
        diffChangeService.execute(systemInfo, since, until, scannerVersion)
        return diffChangeService.findBySystemId(systemId)
    }
}

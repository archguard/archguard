package com.thoughtworks.archguard.systeminfo.controller

import com.thoughtworks.archguard.systeminfo.domain.SystemInfoService
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Controller

@Controller
class SystemInfoCleanupTask(val systemInfoService: SystemInfoService) {

    @Scheduled(cron = "0 0 1 * * *")
    fun dailyCleanupScheduleTask() {
        systemInfoService.cleanupSystemInfo()
    }
}

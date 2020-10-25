package com.thoughtworks.archguard.system_info.controller

import com.thoughtworks.archguard.system_info.domain.SystemInfoService
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Controller

@Controller
class SystemInfoCleanupTask(val systemInfoService: SystemInfoService) {

    @Scheduled(cron = "0 0/2 * * * *")
    fun dailyCleanupScheduleTask() {
        systemInfoService.cleanupSystemInfo()
    }
}
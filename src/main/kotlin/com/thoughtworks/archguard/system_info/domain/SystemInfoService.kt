package com.thoughtworks.archguard.system_info.domain

import com.thoughtworks.archguard.common.exception.EntityNotFoundException
import com.thoughtworks.archguard.system_info.controller.SystemInfoAddMessage
import com.thoughtworks.archguard.system_info.controller.SystemInfoUpdateMessage
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class SystemInfoService(val systemInfoRepository: SystemInfoRepository) {

    fun getSystemInfo(id: Long): SystemInfo {
        return systemInfoRepository.getSystemInfo(id)
                ?: throw EntityNotFoundException(SystemInfo::class.java, id)
    }

    fun getAllSystemInfo(): List<SystemInfo> {
        return systemInfoRepository.getSystemInfoList()
    }

    fun updateSystemInfo(systemInfo: SystemInfo): SystemInfoUpdateMessage {
        return if (systemInfoRepository.updateSystemInfo(systemInfo) == 1) {
            SystemInfoUpdateMessage(true, "update system info success")
        } else {
            SystemInfoUpdateMessage(false, "update error")
        }
    }

    fun addSystemInfo(systemInfo: SystemInfo): SystemInfoAddMessage {
        return if (systemInfoRepository.queryBysystemName(systemInfo.systemName) == 0) {
            val id = systemInfoRepository.addSystemInfo(systemInfo)
            SystemInfoAddMessage(true, "add new system info success", id)
        } else {
            SystemInfoAddMessage(false, "There is already system info", 0)
        }
    }

    @Transactional
    fun deleteSystemInfo(id: Long) {
        systemInfoRepository.deleteSystemInfo(id)
        systemInfoRepository.deleteSystemInfoRelated(id)
    }
}

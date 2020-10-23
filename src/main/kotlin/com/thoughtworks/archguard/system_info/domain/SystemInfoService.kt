package com.thoughtworks.archguard.system_info.domain

import com.thoughtworks.archguard.common.exception.DuplicateResourceException
import com.thoughtworks.archguard.common.exception.EntityNotFoundException
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

    fun updateSystemInfo(systemInfo: SystemInfo) {
        val updatedRow = systemInfoRepository.updateSystemInfo(systemInfo)
        if (updatedRow != 1)
            throw EntityNotFoundException(SystemInfo::class.java, systemInfo.id)
    }

    fun addSystemInfo(systemInfo: SystemInfo): Long {
        if (systemInfoRepository.queryBysystemName(systemInfo.systemName) == 0) {
            return systemInfoRepository.addSystemInfo(systemInfo)
        } else {
            throw DuplicateResourceException("There is already system info")
        }
    }

    @Transactional
    fun deleteSystemInfo(id: Long) {
        systemInfoRepository.deleteSystemInfo(id)
        systemInfoRepository.deleteSystemInfoRelated(id)
    }
}

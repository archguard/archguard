package com.thoughtworks.archguard.system_info.domain

import com.thoughtworks.archguard.common.exception.DuplicateResourceException
import com.thoughtworks.archguard.common.exception.EntityNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class SystemInfoService(val systemInfoRepository: SystemInfoRepository,
                        val analysisClientProxy: AnalysisClientProxy) {

    fun getSystemInfo(id: Long): SystemInfo {
        return systemInfoRepository.getSystemInfo(id)
                ?: throw EntityNotFoundException(SystemInfo::class.java, id)
    }

    fun getAllSystemInfo(): List<SystemInfo> {
        return systemInfoRepository.getSystemInfoList()
    }

    @Transactional
    fun updateSystemInfo(systemInfo: SystemInfo) {
        val updatedRow = systemInfoRepository.updateSystemInfo(systemInfo)
        if (updatedRow != 1) {
            throw EntityNotFoundException(SystemInfo::class.java, systemInfo.id)
        } else {
            analysisClientProxy.refreshThresholdCache()
        }
    }

    @Transactional
    fun addSystemInfo(systemInfo: SystemInfo): Long {
        if (systemInfoRepository.queryBysystemName(systemInfo.systemName) == 0) {
            val id = systemInfoRepository.addSystemInfo(systemInfo)
            analysisClientProxy.refreshThresholdCache()
            return id
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

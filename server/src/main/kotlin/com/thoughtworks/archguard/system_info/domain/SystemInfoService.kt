package com.thoughtworks.archguard.system_info.domain

import com.thoughtworks.archguard.common.exception.DuplicateResourceException
import com.thoughtworks.archguard.common.exception.EntityNotFoundException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class SystemInfoService(
    val systemInfoRepository: SystemInfoRepository,
    var analysisClientProxy: AnalysisClientProxy
) {

    val logger: Logger = LoggerFactory.getLogger(SystemInfoService::class.java)

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
        if (systemInfoRepository.queryBySystemName(systemInfo.systemName) == 0) {
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

        // todo: temp clean for tests
        systemInfoRepository.deleteSystemInfoRelated()
    }

    fun cleanupSystemInfo() {
        logger.info("Daily system info clean up task start ...")
        systemInfoRepository.deleteSystemInfoRelated()
        logger.info("Daily system info clean up task done .")
    }
}

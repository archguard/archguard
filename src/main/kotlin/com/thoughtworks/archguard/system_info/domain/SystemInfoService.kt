package com.thoughtworks.archguard.system_info.domain

import com.thoughtworks.archguard.common.exception.EntityNotFoundException
import com.thoughtworks.archguard.system_info.controller.SystemInfoAddMessage
import com.thoughtworks.archguard.system_info.controller.SystemInfoUpdateMessage
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class SystemInfoService {

    @Autowired
    lateinit var systemInfoRepository: SystemInfoRepository

    @Autowired
    lateinit var systemInfoMapper: SystemInfoMapper

    fun getSystemInfo(id: Long): SystemInfoDTO {
        val SystemInfo = systemInfoRepository.getSystemInfo(id) ?: throw EntityNotFoundException(SystemInfo::class.java, id)
        return systemInfoMapper.toDTO(SystemInfo)
    }

    fun getAllSystemInfo(): List<SystemInfoDTO> {
        return systemInfoRepository.getSystemInfoList().map ( systemInfoMapper::toDTO )
    }

    fun updateSystemInfo(systemInfoDTO: SystemInfoDTO): SystemInfoUpdateMessage {
        val systemInfo: SystemInfo = systemInfoMapper.fromDTO(systemInfoDTO)
        return if (systemInfoRepository.updateSystemInfo(systemInfo) == 1) {
            SystemInfoUpdateMessage(true, "update system info success")
        } else {
            SystemInfoUpdateMessage(false, "update error")
        }
    }

    fun addSystemInfo(systemInfoDTO: SystemInfoDTO): SystemInfoAddMessage {
        val systemInfo: SystemInfo = systemInfoMapper.fromDTO(systemInfoDTO)
        return if (systemInfoRepository.queryBysystemName(systemInfo.systemName) == 0) {
            val id = systemInfoRepository.addSystemInfo(systemInfo)
            SystemInfoAddMessage(true, "add new system info success", id)
        } else {
            SystemInfoAddMessage(false, "There is already system info", 0)
        }
    }

    fun deleteSystemInfo(id: Long) {
        systemInfoRepository.deleteSystemInfo(id)
    }
}

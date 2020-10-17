package com.thoughtworks.archguard.system_info.domain

import com.thoughtworks.archguard.system_info.infrastracture.AESCrypt
import org.springframework.stereotype.Component

@Component
class SystemInfoMapper {

    fun fromDTO(dto: SystemInfoDTO): SystemInfo {
        return SystemInfo(id = dto.id, systemName = dto.systemName, sql = dto.sql, username = dto.username,
                password = AESCrypt.encrypt(dto.password), repoType = dto.repoType, scanned = dto.scanned,
                qualityGateProfileId = dto.qualityGateProfileId,
                repo = dto.repo.joinToString(","))

    }

    fun toDTO(info: SystemInfo): SystemInfoDTO {
        return SystemInfoDTO(id = info.id, systemName = info.systemName, sql = info.sql, username = info.username,
                password = info.password, repoType = info.repoType, scanned = info.scanned,
                qualityGateProfileId = info.qualityGateProfileId,
                repo = info.repo.split(','), updatedTime = info.updatedTime)
    }
}

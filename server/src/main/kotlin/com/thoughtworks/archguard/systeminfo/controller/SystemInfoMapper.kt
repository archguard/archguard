package com.thoughtworks.archguard.systeminfo.controller

import com.thoughtworks.archguard.systeminfo.domain.SystemInfo
import com.thoughtworks.archguard.systeminfo.infrastracture.AESCrypt
import org.springframework.stereotype.Component

@Component
class SystemInfoMapper {

    fun fromDTO(dto: SystemInfoCreateDTO): SystemInfo {
        return SystemInfo(
            systemName = dto.systemName, username = dto.username,
            password = AESCrypt.encrypt(dto.password), repoType = dto.repoType,
            language = dto.language, codePath = dto.codePath,
            repo = dto.repo.joinToString(","),
            badSmellThresholdSuiteId = dto.badSmellThresholdSuiteId, branch = dto.branch
        )
    }

    fun fromDTO(dto: SystemInfoUpdateDTO): SystemInfo {
        return SystemInfo(
            id = dto.id, systemName = dto.systemName, username = dto.username,
            password = AESCrypt.encrypt(dto.password), repoType = dto.repoType,
            language = dto.language, codePath = dto.codePath,
            repo = dto.repo.joinToString(","),
            badSmellThresholdSuiteId = dto.badSmellThresholdSuiteId, branch = dto.branch
        )
    }

    fun toDTO(info: SystemInfo): SystemInfoDTO {
        return SystemInfoDTO(
            id = info.id, systemName = info.systemName, username = info.username,
            password = info.password, repoType = info.repoType, scanned = info.scanned,
            qualityGateProfileId = info.qualityGateProfileId, language = info.language, codePath = info.codePath,
            repo = info.repo.split(','), updatedTime = info.updatedTime?.time,
            badSmellThresholdSuiteId = info.badSmellThresholdSuiteId, branch = info.branch
        )
    }
}

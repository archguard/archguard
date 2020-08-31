package com.thoughtworks.archguard.project_info.domain

import com.thoughtworks.archguard.project_info.infrastracture.AESCrypt
import org.springframework.stereotype.Component

@Component
class ProjectInfoMapper {

    fun fromDTO(dto: ProjectInfoDTO): ProjectInfo {
        return ProjectInfo(id = dto.id, projectName = dto.projectName, sql = dto.sql, username = dto.username,
                password = AESCrypt.encrypt(dto.password), repoType = dto.repoType, scanned = dto.scanned,
                qualityGateProfileId = dto.qualityGateProfileId,
                repo = dto.repo.joinToString(","))

    }

    fun toDTO(info: ProjectInfo): ProjectInfoDTO {
        return ProjectInfoDTO(id = info.id, projectName = info.projectName, sql = info.sql, username = info.username,
                password = info.password, repoType = info.repoType, scanned = info.scanned,
                qualityGateProfileId = info.qualityGateProfileId,
                repo = info.repo.split(','))
    }
}

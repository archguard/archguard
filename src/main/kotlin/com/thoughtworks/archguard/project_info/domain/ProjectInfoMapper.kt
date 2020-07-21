package com.thoughtworks.archguard.project_info.domain

import com.thoughtworks.archguard.project_info.infrastracture.AESCrypt
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class ProjectInfoMapper {
    @Autowired
    lateinit var aesCrypt: AESCrypt

    fun fromDTO(dto: ProjectInfoDTO): ProjectInfo {
        return ProjectInfo(id = dto.id, projectName = dto.projectName, sql = dto.sql, username = dto.username,
                password = aesCrypt.encrypt(dto.password), repoType = dto.repoType,
                repo = dto.repo.joinToString(","))

    }

    fun toDTO(info: ProjectInfo): ProjectInfoDTO {
        return ProjectInfoDTO(id = info.id, projectName = info.projectName, sql = info.sql, username = info.username,
                password = aesCrypt.decrypt(info.password), repoType = info.repoType,
                repo = info.repo.split(','))
    }
}

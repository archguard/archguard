package com.thoughtworks.archguard.project_info.domain

class ProjectInfoMapper {
    companion object {
        fun fromDTO(dto: ProjectInfoDTO): ProjectInfo {
            return ProjectInfo(id = dto.id, projectName = dto.projectName, sql = dto.sql, username = dto.username,
                    password = dto.password, repoType = dto.repoType,
                    repo = dto.repo.joinToString(","))

        }

        fun toDTO(info: ProjectInfo): ProjectInfoDTO {
            return ProjectInfoDTO(id = info.id, projectName = info.projectName, sql = info.sql, username = info.username,
                    password = info.password, repoType = info.repoType,
                    repo = info.repo.split(','))
        }
    }
}

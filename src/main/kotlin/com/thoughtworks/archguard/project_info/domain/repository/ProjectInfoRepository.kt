package com.thoughtworks.archguard.project_info.domain.repository

import com.thoughtworks.archguard.project_info.domain.dto.ProjectInfoAddDTO
import com.thoughtworks.archguard.project_info.domain.dto.ProjectInfoDTO

interface ProjectInfoRepository {
    fun getProjectInfo(): ProjectInfoDTO?

    fun updateProjectInfo(projectInfo: ProjectInfoDTO): Int

    fun addProjectInfo(projectInfo: ProjectInfoAddDTO): String

    fun querySizeOfProjectInfo(): Int

}
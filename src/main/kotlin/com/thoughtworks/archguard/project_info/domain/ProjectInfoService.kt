package com.thoughtworks.archguard.project_info.domain

import com.thoughtworks.archguard.common.exception.EntityNotFoundException
import com.thoughtworks.archguard.project_info.controller.ProjectInfoAddMessage
import com.thoughtworks.archguard.project_info.controller.ProjectInfoUpdateMessage
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ProjectInfoService {

    @Autowired
    lateinit var projectInfoRepository: ProjectInfoRepository

    @Autowired
    lateinit var projectInfoMapper: ProjectInfoMapper

    fun getProjectInfo(id: Long): ProjectInfoDTO {
        val projectInfo = projectInfoRepository.getProjectInfo(id) ?: throw EntityNotFoundException(ProjectInfo::class.java, id)
        return projectInfoMapper.toDTO(projectInfo)
    }

    fun getAllProjectInfo(): List<ProjectInfoDTO> {
        return projectInfoRepository.getProjectInfoList().map ( projectInfoMapper::toDTO )
    }

    fun updateProjectInfo(projectInfoDTO: ProjectInfoDTO): ProjectInfoUpdateMessage {
        val projectInfo: ProjectInfo = projectInfoMapper.fromDTO(projectInfoDTO)
        return if (projectInfoRepository.updateProjectInfo(projectInfo) == 1) {
            ProjectInfoUpdateMessage(true, "update project info success")
        } else {
            ProjectInfoUpdateMessage(false, "update error")
        }
    }

    fun addProjectInfo(projectInfoDTO: ProjectInfoDTO): ProjectInfoAddMessage {
        val projectInfo: ProjectInfo = projectInfoMapper.fromDTO(projectInfoDTO)
        return if (projectInfoRepository.queryByProjectName(projectInfo.projectName) == 0) {
            val id = projectInfoRepository.addProjectInfo(projectInfo)
            ProjectInfoAddMessage(true, "add new project info success", id)
        } else {
            ProjectInfoAddMessage(false, "There is already project info", 0)
        }
    }

    fun deleteProjectInfo(id: Long) {
        projectInfoRepository.deleteProjectInfo(id)
    }
}

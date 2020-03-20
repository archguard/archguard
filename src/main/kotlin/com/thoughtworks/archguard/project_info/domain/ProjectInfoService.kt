package com.thoughtworks.archguard.project_info.domain

import com.thoughtworks.archguard.project_info.controller.ProjectInfoAddMessage
import com.thoughtworks.archguard.project_info.controller.ProjectInfoUpdateMessage
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ProjectInfoService {

    @Autowired
    lateinit var projectInfoRepository: ProjectInfoRepository

    fun getProjectInfo(): ProjectInfo = projectInfoRepository.getProjectInfo() ?: ProjectInfo("", "", ArrayList())

    fun updateProjectInfo(projectInfo: ProjectInfo) =
            if (projectInfoRepository.updateProjectInfo(projectInfo) == 1) {
                ProjectInfoUpdateMessage(true, "update project info success")
            } else {
                ProjectInfoUpdateMessage(false, "update error")
            }

    fun addProjectInfo(projectInfo: ProjectInfo) =
            if (projectInfoRepository.querySizeOfProjectInfo() == 0) {
                val id = projectInfoRepository.addProjectInfo(projectInfo)
                ProjectInfoAddMessage(true, "add new project info success", id)
            } else {
                ProjectInfoAddMessage(false, "There is already project info", "null")
            }


}
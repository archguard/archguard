package com.thoughtworks.archguard.project_info.controller

import com.thoughtworks.archguard.project_info.domain.dto.ProjectInfoDTO
import com.thoughtworks.archguard.project_info.domain.service.ProjectInfoService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/")
class ProjectInfoController {

    @Autowired
    lateinit var projectInfoService: ProjectInfoService

    @GetMapping("/project/info")
    fun getProjectInfo() = projectInfoService.getProjectInfo()

    @PutMapping("/project/info")
    fun updateProjectInfo(@RequestBody projectInfo: ProjectInfoDTO) = projectInfoService.updateProjectInfo(projectInfo)
}
package com.thoughtworks.archguard.project_info.controller

import com.thoughtworks.archguard.project_info.domain.ProjectInfo
import com.thoughtworks.archguard.project_info.domain.ProjectInfoDTO
import com.thoughtworks.archguard.project_info.domain.ProjectInfoService
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
    fun updateProjectInfo(@RequestBody projectInfoDTO: ProjectInfoDTO) = projectInfoService.updateProjectInfo(projectInfoDTO)

    @PostMapping("/project/info")
    fun addProjectInfo(@RequestBody projectInfoDTO: ProjectInfoDTO) = projectInfoService.addProjectInfo(projectInfoDTO)
}

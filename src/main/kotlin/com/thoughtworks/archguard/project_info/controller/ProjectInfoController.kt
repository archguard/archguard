package com.thoughtworks.archguard.project_info.controller

import com.thoughtworks.archguard.project_info.domain.ProjectInfoDTO
import com.thoughtworks.archguard.project_info.domain.ProjectInfoService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/project-info")
class ProjectInfoController {

    @Autowired
    lateinit var projectInfoService: ProjectInfoService

    @GetMapping("/{id}")
    fun getProjectInfo(@PathVariable("id") id: Long) = projectInfoService.getProjectInfo(id)

    @GetMapping
    fun getAllProjectInfo() = projectInfoService.getAllProjectInfo()

    @PutMapping
    fun updateProjectInfo(@RequestBody projectInfoDTO: ProjectInfoDTO) = projectInfoService.updateProjectInfo(projectInfoDTO)

    @PostMapping
    fun addProjectInfo(@RequestBody projectInfoDTO: ProjectInfoDTO) = projectInfoService.addProjectInfo(projectInfoDTO)

    @DeleteMapping("/{id}")
    fun deleteProjectInfo(@PathVariable("id") id: Long) = projectInfoService.deleteProjectInfo(id)
}

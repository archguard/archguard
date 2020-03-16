package com.thoughtworks.archguard.project_info.controller

import com.thoughtworks.archguard.project_info.domain.service.ProjectInfoService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/")
class ProjectInfoController {

    @Autowired
    lateinit var projectInfoService: ProjectInfoService

    @GetMapping("/project/info")
    fun getProjectInfo() = projectInfoService.getProjectInfo()
}
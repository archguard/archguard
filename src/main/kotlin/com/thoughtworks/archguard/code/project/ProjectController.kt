package com.thoughtworks.archguard.code.project

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/systems/{systemId}/project")
class ProjectController(val projectService: ProjectService) {

    @GetMapping("/sca")
    fun getDependencies(@PathVariable("systemId") systemId: Long): List<CompositionDependency> {
        return projectService.getProjectDependencies(systemId)
    }
}

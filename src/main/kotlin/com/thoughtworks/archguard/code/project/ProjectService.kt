package com.thoughtworks.archguard.code.project

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ProjectService {
    @Autowired
    lateinit var projectRepository: ProjectRepository

    fun getProjectDependencies(systemId: Long): List<CompositionDependency> {
        return projectRepository.getProjectDependencies(systemId)
    }
}

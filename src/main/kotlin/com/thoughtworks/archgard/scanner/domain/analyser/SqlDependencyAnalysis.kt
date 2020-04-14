package com.thoughtworks.archgard.scanner.domain.analyser

import com.thoughtworks.archgard.scanner.domain.project.ProjectRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class SqlDependencyAnalysis(@Autowired val projectRepository: ProjectRepository) {
    fun analyse() {
        val project = projectRepository.getProjectInfo().build()

    }

}

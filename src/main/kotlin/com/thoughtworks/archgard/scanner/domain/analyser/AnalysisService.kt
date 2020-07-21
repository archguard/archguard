package com.thoughtworks.archgard.scanner.domain.analyser

import com.thoughtworks.archgard.scanner.domain.project.ProjectOperator
import com.thoughtworks.archgard.scanner.domain.project.ProjectRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class AnalysisService(@Autowired val projectRepository: ProjectRepository) {
    fun getProjectOperator(): ProjectOperator {
        val projectInfo =
                projectRepository.getProjectInfo()
        return ProjectOperator(projectInfo)
    }
}

package com.thoughtworks.archgard.scanner.domain.analyser

import com.thoughtworks.archgard.scanner.domain.project.ProjectInfo
import com.thoughtworks.archgard.scanner.domain.project.ProjectOperator
import com.thoughtworks.archgard.scanner.domain.project.ProjectRepository
import com.thoughtworks.archguard.common.exception.EntityNotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class AnalysisService(@Autowired val projectRepository: ProjectRepository) {
    fun getProjectOperator(id: Long): ProjectOperator {
        val projectInfo = projectRepository.getProjectInfo(id)
                ?: throw EntityNotFoundException(ProjectInfo::class.java, id)
        return ProjectOperator(projectInfo)
    }
}

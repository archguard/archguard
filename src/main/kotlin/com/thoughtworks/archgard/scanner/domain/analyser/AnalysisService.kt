package com.thoughtworks.archgard.scanner.domain.analyser

import com.thoughtworks.archgard.scanner.domain.project.ProjectInfo
import com.thoughtworks.archgard.scanner.domain.project.ProjectOperator
import com.thoughtworks.archgard.scanner.domain.project.ProjectRepository
import com.thoughtworks.archguard.common.exception.EntityNotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.io.FileNotFoundException
import java.nio.file.Files
import java.nio.file.Paths

@Service
class AnalysisService(@Autowired val projectRepository: ProjectRepository) {
    fun getProjectOperator(id: Long): ProjectOperator {
        val projectInfo = projectRepository.getProjectInfo(id)
                ?: throw EntityNotFoundException(ProjectInfo::class.java, id)
        checkAnalysable(projectInfo)
        return ProjectOperator(projectInfo)
    }

    fun checkAnalysable(projectInfo: ProjectInfo) {
        if (projectInfo.repoType == "ZIP") {
            projectInfo.getRepoList().forEach { repo ->
                run {
                    if (!Files.exists(Paths.get(repo))) {
                        throw FileNotFoundException("zip file has been deleted: $repo")
                    }
                }
            }
        }
    }
}

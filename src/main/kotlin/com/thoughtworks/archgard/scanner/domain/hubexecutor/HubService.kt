package com.thoughtworks.archgard.scanner.domain.hubexecutor

import com.thoughtworks.archgard.scanner.domain.project.ProjectRepository
import com.thoughtworks.archgard.scanner.domain.ScanContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class HubService {
    @Autowired
    private lateinit var manager: ScannerManager

    @Autowired
    private lateinit var hubRepository: ProjectRepository

    fun doScan() {
        val gitRepo = hubRepository.getProjectInfo().gitRepo
        val context = ScanContext(gitRepo, createTempDir())

        val hubExecutor = HubExecutor(context, manager)

        try {
            hubExecutor.execute()
        } finally {
            hubExecutor.clean()
        }
    }
}
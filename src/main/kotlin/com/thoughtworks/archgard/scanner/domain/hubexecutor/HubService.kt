package com.thoughtworks.archgard.scanner.domain.hubexecutor

import com.thoughtworks.archgard.scanner.domain.ScanContext
import com.thoughtworks.archgard.scanner.domain.project.ProjectRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class HubService {
    @Autowired
    private lateinit var manager: ScannerManager

    private val log = LoggerFactory.getLogger(HubService::class.java)

    @Autowired
    private lateinit var hubRepository: ProjectRepository

    fun doScan() {
        val gitRepo = hubRepository.getProjectInfo().gitRepo
        val workspace = createTempDir()
        log.info("workspace is: {}, gitRepo is: {}", workspace.toPath().toString(), gitRepo)

        val context = ScanContext(gitRepo, workspace)
//        val hubExecutor = HubExecutor(context, manager)
//        hubExecutor.execute()
    }
}
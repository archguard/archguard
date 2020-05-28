package com.thoughtworks.archgard.scanner.domain.hubexecutor

import com.thoughtworks.archgard.scanner.domain.ScanContext
import com.thoughtworks.archgard.scanner.domain.config.repository.ConfigureRepository
import com.thoughtworks.archgard.scanner.domain.project.ProjectRepository
import com.thoughtworks.archgard.scanner.infrastructure.client.EvaluationReportClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class HubService {
    @Autowired
    private lateinit var manager: ScannerManager

    private var isRunning: Boolean = false

    @Autowired
    private lateinit var hubRepository: ProjectRepository

    @Autowired
    private lateinit var evaluationReportClient: EvaluationReportClient

    @Autowired
    private lateinit var configureRepository: ConfigureRepository

    fun doScan(): Boolean {
        if (!isRunning) {
            isRunning = true
            val project = hubRepository.getProjectInfo().build()
            val config = configureRepository.getToolConfigures()

            val context = ScanContext(project.repo, project.buildTool, project.workspace, config)
            val hubExecutor = HubExecutor(context, manager)
            hubExecutor.execute()
            isRunning = false
        }
        return isRunning
    }

    fun evaluate(type: String): Boolean {
        doScan()
        if (!isRunning) {
            evaluationReportClient.generate(type)
        }
        return isRunning
    }

    fun getEvaluationStatus(type: String): Boolean {
        return isRunning;
    }

}
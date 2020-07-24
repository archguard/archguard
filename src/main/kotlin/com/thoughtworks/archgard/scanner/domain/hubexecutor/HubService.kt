package com.thoughtworks.archgard.scanner.domain.hubexecutor

import com.thoughtworks.archgard.scanner.domain.ScanContext
import com.thoughtworks.archgard.scanner.domain.analyser.AnalysisService
import com.thoughtworks.archgard.scanner.domain.config.repository.ConfigureRepository
import com.thoughtworks.archgard.scanner.infrastructure.client.EvaluationReportClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import kotlin.concurrent.thread

@Service
class HubService {
    @Autowired
    private lateinit var manager: ScannerManager

    @Volatile
    private var isRunning: Boolean = false

    @Autowired
    private lateinit var analysisService: AnalysisService

    @Autowired
    private lateinit var evaluationReportClient: EvaluationReportClient

    @Autowired
    private lateinit var configureRepository: ConfigureRepository

    fun doScanIfNotRunning(): Boolean {
        if (!isRunning) {
            isRunning = true
            doScan()
            isRunning = false
        }
        return isRunning
    }

    fun evaluate(type: String): Boolean {
        if (!isRunning) {
            isRunning = true
            thread {
                doScan()
                isRunning = false
                evaluationReportClient.generate(type)
            }
        }
        return isRunning
    }

    private fun doScan() {
        val config = configureRepository.getToolConfigures()
        val projectOperator = analysisService.getProjectOperator()
        projectOperator.cloneAndBuildAllRepo()
        projectOperator.compiledProjectMap.forEach { (repo, compiledProject) ->
            val context = ScanContext(repo, compiledProject.buildTool, compiledProject.workspace, config)
            val hubExecutor = HubExecutor(context, manager)
            hubExecutor.execute()
        }
    }

    fun getEvaluationStatus(type: String): Boolean {
        return isRunning;
    }

}

package com.thoughtworks.archgard.scanner.domain.hubexecutor

import com.thoughtworks.archgard.scanner.domain.ScanContext
import com.thoughtworks.archgard.scanner.domain.analyser.AnalysisService
import com.thoughtworks.archgard.scanner.domain.config.repository.ConfigureRepository
import com.thoughtworks.archgard.scanner.infrastructure.client.EvaluationReportClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.concurrent.CopyOnWriteArraySet
import kotlin.concurrent.thread

@Service
class HubService {
    @Autowired
    private lateinit var manager: ScannerManager

    @Volatile
    private var isRunning: Boolean = false

    private val concurrentSet: CopyOnWriteArraySet<Long> = CopyOnWriteArraySet()

    @Autowired
    private lateinit var analysisService: AnalysisService

    @Autowired
    private lateinit var evaluationReportClient: EvaluationReportClient

    @Autowired
    private lateinit var configureRepository: ConfigureRepository

    fun doScanIfNotRunning(id: Long): Boolean {
        if (!concurrentSet.contains(id)) {
            concurrentSet.add(id)
            doScan(id)
            concurrentSet.remove(id)
        }
        return concurrentSet.contains(id)
    }

    fun evaluate(type: String, id: Long): Boolean {
        if (!concurrentSet.contains(id)) {
            concurrentSet.add(id)
            thread {
                doScan(id)
                concurrentSet.remove(id)
                evaluationReportClient.generate(type)
            }
        }
        return concurrentSet.contains(id)
    }

    private fun doScan(id: Long) {
        val config = configureRepository.getToolConfigures()
        val systemOperator = analysisService.getSystemOperator(id)
        systemOperator.cloneAndBuildAllRepo()
        systemOperator.compiledProjectMap.forEach { (repo, compiledProject) ->
            val context = ScanContext(repo, compiledProject.buildTool, compiledProject.workspace, config)
            val hubExecutor = HubExecutor(context, manager)
            hubExecutor.execute()
        }
    }

    fun getEvaluationStatus(type: String, id: Long): Boolean {
        return concurrentSet.contains(id)
    }

}

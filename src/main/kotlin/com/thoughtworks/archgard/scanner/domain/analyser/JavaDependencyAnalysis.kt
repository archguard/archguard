package com.thoughtworks.archgard.scanner.domain.analyser

import com.thoughtworks.archgard.scanner.domain.hubexecutor.HubService
import com.thoughtworks.archgard.scanner.domain.tools.JavaByteCodeTool
import com.thoughtworks.archgard.scanner.domain.tools.TableUsedTool
import com.thoughtworks.archgard.scanner.infrastructure.client.AnalysisModuleClient
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class JavaDependencyAnalysis(@Value("\${spring.datasource.url}") val dbUrl: String,
                             @Value("\${spring.datasource.username}") val username: String,
                             @Value("\${spring.datasource.password}") val password: String,
                             @Autowired val hubService: HubService,
                             @Autowired val analysisService: AnalysisService,
                             @Autowired val analysisModuleClient: AnalysisModuleClient) {
    private val log = LoggerFactory.getLogger(JavaDependencyAnalysis::class.java)

    fun analyse(id: Long) {
        log.info("start scan java analysis")
        val systemOperator = analysisService.getSystemOperator(id)
        val url = dbUrl.replace("://", "://" + username + ":" + password + "@")

        systemOperator.cloneAndBuildAllRepo()
        val javaByteCodeTool = JavaByteCodeTool(systemOperator.workspace, url, id)
        javaByteCodeTool.analyse()
        log.info("finished scan java byte code")

        val tableUsedTool = TableUsedTool(systemOperator.workspace, systemOperator.sql)
        tableUsedTool.analyse()
        log.info("finished scan tables")

        analysisModuleClient.autoDefine(id)
        log.info("finished logic module auto define")

        hubService.doScanIfNotRunning(id);
        log.info("finished level 1 scanners")

        analysisModuleClient.metricsAnalysis(id)
        log.info("finished level 2 analysis metrics")
    }
}

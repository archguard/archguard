package com.thoughtworks.archgard.scanner.domain.analyser

import com.thoughtworks.archgard.scanner.domain.exception.AnalysisException
import com.thoughtworks.archgard.scanner.domain.exception.EntityNotFoundException
import com.thoughtworks.archgard.scanner.domain.hubexecutor.HubService
import com.thoughtworks.archgard.scanner.domain.system.ScannedType
import com.thoughtworks.archgard.scanner.domain.system.SystemInfo
import com.thoughtworks.archgard.scanner.domain.system.SystemInfoRepository
import com.thoughtworks.archgard.scanner.infrastructure.client.AnalysisModuleClient
import com.thoughtworks.archgard.scanner.infrastructure.client.Scanner2Client
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.concurrent.CopyOnWriteArraySet
import java.util.concurrent.Executors

@Service
class JavaDependencyAnalysis(@Value("\${spring.datasource.url}") val dbUrl: String,
                             @Value("\${spring.datasource.username}") val username: String,
                             @Value("\${spring.datasource.password}") val password: String,
                             val hubService: HubService,
                             val systemInfoRepository: SystemInfoRepository,
                             val analysisService: AnalysisService,
                             val analysisModuleClient: AnalysisModuleClient,
                             val scanner2Client: Scanner2Client) {
    private val log = LoggerFactory.getLogger(JavaDependencyAnalysis::class.java)
    private val runningSystemIdSet = CopyOnWriteArraySet<Long>()
    private val executor = Executors.newFixedThreadPool(5)

    fun asyncAnalyse(systemId: Long) {
        if (runningSystemIdSet.contains(systemId)) {
            throw AnalysisException("this system is scanning: $systemId")
        }
        executor.execute {
            val systemInfo = getSystemInfo(systemId)
            try {
                startScanSystem(systemInfo)
                analyse(systemId)
                stopScanSystem(systemInfo, ScannedType.SCANNED)
            } catch (e: Exception) {
                log.error(e.message)
                stopScanSystem(systemInfo, ScannedType.FAILED)
            }
        }
    }

    fun analyse(systemId: Long) {
        log.info("************************************")
        log.info(" Start scan java analysis")
        log.info("************************************")
        val url = dbUrl.replace("://", "://$username:$password@")

        hubService.doScanIfNotRunning(systemId, url);
        log.info("************************************")
        log.info(" Finished level 1 scanners")
        log.info("************************************")

        analysisModuleClient.autoDefine(systemId)
        log.info("************************************")
        log.info(" Finished logic module auto define")
        log.info("************************************")

        scanner2Client.level2MetricsAnalysis(systemId)
        log.info("************************************")
        log.info(" Finished level 2 analysis metrics")
        log.info("************************************")

        analysisModuleClient.badSmellDashboard(systemId)
        log.info("************************************")
        log.info(" Finished bad smell dashboard")
        log.info("************************************")
    }

    private fun getSystemInfo(systemId: Long): SystemInfo {
        return systemInfoRepository.getSystemInfo(systemId)
                ?: throw EntityNotFoundException("system info is not exists: $systemId")
    }

    private fun startScanSystem(systemInfo: SystemInfo) {
        log.info("SET SYSTEM INFO {} SCAN FLAG TO :{}", systemInfo.id, ScannedType.SCANNING)
        systemInfo.scanned = ScannedType.SCANNING
        systemInfoRepository.updateSystemInfo(systemInfo)
        this.runningSystemIdSet.add(systemInfo.id)
    }

    private fun stopScanSystem(systemInfo: SystemInfo, scannedType: ScannedType) {
        log.info("SET SYSTEM INFO {} SCAN FLAG TO :{}", systemInfo.id, scannedType)
        systemInfo.scanned = scannedType
        systemInfoRepository.updateSystemInfo(systemInfo)
        this.runningSystemIdSet.remove(systemInfo.id)
    }
}

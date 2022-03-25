package com.thoughtworks.archguard.scanner.domain.analyser

import com.thoughtworks.archguard.scanner.domain.exception.AnalysisException
import com.thoughtworks.archguard.scanner.domain.exception.EntityNotFoundException
import com.thoughtworks.archguard.scanner.domain.hubexecutor.HubExecutorService
import com.thoughtworks.archguard.scanner.domain.system.ScannedType
import com.thoughtworks.archguard.scanner.domain.system.SystemInfo
import com.thoughtworks.archguard.scanner.domain.system.SystemInfoRepository
import com.thoughtworks.archguard.scanner.infrastructure.client.AnalysisModuleClient
import com.thoughtworks.archguard.scanner.infrastructure.client.Scanner2Client
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.concurrent.CopyOnWriteArraySet
import java.util.concurrent.Executors
import javax.annotation.PostConstruct

@Service
class ArchitectureDependencyAnalysis(@Value("\${spring.datasource.url}") val dbUrl: String,
                                     @Value("\${spring.datasource.username}") val username: String,
                                     @Value("\${spring.datasource.password}") val password: String,
                                     @Value("\${init.scan.status}") val updateScanStatus: Boolean,
                                     val hubService: HubExecutorService,
                                     val systemInfoRepository: SystemInfoRepository,
                                     val analysisModuleClient: AnalysisModuleClient,
                                     val scanner2Client: Scanner2Client) {
    private val log = LoggerFactory.getLogger(ArchitectureDependencyAnalysis::class.java)
    private val runningSystemIdSet = CopyOnWriteArraySet<Long>()
    private val executor = Executors.newFixedThreadPool(5)

    @PostConstruct
    fun postConstruct() {
        if (updateScanStatus) {
            systemInfoRepository.updateScanningSystemToScanFail();
        }
    }

    fun asyncAnalyse(systemId: Long) {
        if (runningSystemIdSet.contains(systemId)) {
            throw AnalysisException("this system is scanning: $systemId")
        }

        systemInfoRepository.removeNotClearRelatedData(systemId)
        executor.execute {
            val systemInfo = getSystemInfo(systemId)
            try {
                startScanSystem(systemInfo)
                analyse(systemId, systemInfo.language)
                stopScanSystem(systemInfo, ScannedType.SCANNED)
            } catch (e: Exception) {
                log.error("Exception in asyncAnalyse: {}", e)
                stopScanSystem(systemInfo, ScannedType.FAILED)
            }
        }
    }

    fun analyse(systemId: Long, language: String) {
        log.info("************************************")
        log.info(" Start scan analysis")
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

    fun cancelAnalyse(systemId: Long) {
        val systemInfo = getSystemInfo(systemId)
        this.stopScanSystem(systemInfo, ScannedType.FAILED)
    }
}

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
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.concurrent.CopyOnWriteArraySet
import java.util.concurrent.Executors

@Service
class JavaDependencyAnalysis(@Value("\${spring.datasource.url}") val dbUrl: String,
                             @Value("\${spring.datasource.username}") val username: String,
                             @Value("\${spring.datasource.password}") val password: String,
                             @Autowired val hubService: HubService,
                             @Autowired val systemInfoRepository: SystemInfoRepository,
                             @Autowired val analysisService: AnalysisService,
                             @Autowired val analysisModuleClient: AnalysisModuleClient,
                             @Autowired val scanner2Client: Scanner2Client) {
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
            } catch (e: Exception) {
                log.error(e.message)
            } finally {
                finishScanSystem(systemInfo)
            }
        }
    }

    fun analyse(systemId: Long) {
        log.info("start scan java analysis")
        val url = dbUrl.replace("://", "://$username:$password@")

        hubService.doScanIfNotRunning(systemId, url);
        log.info("finished level 1 scanners")

        analysisModuleClient.autoDefine(systemId)
        log.info("finished logic module auto define")

        scanner2Client.level2MetricsAnalysis(systemId)
        log.info("finished level 2 analysis metrics")
    }

    private fun getSystemInfo(systemId: Long): SystemInfo {
        return systemInfoRepository.getSystemInfo(systemId)
                ?: throw EntityNotFoundException("system info is not exists: $systemId")
    }

    private fun startScanSystem(systemInfo: SystemInfo) {
        systemInfo.scanned = ScannedType.SCANNING
        systemInfoRepository.updateSystemInfo(systemInfo)
        this.runningSystemIdSet.add(systemInfo.id)
    }

    private fun finishScanSystem(systemInfo: SystemInfo) {
        systemInfo.scanned = ScannedType.SCANNED
        systemInfoRepository.updateSystemInfo(systemInfo)
        this.runningSystemIdSet.remove(systemInfo.id)
    }
}

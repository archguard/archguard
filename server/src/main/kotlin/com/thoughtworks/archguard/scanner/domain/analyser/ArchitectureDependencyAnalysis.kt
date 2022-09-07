package com.thoughtworks.archguard.scanner.domain.analyser

import com.thoughtworks.archguard.scanner.domain.exception.AnalysisException
import com.thoughtworks.archguard.scanner.domain.exception.EntityNotFoundException
import com.thoughtworks.archguard.scanner.domain.hubexecutor.HubExecutorService
import com.thoughtworks.archguard.scanner.domain.system.ScannedType
import com.thoughtworks.archguard.scanner.domain.system.SystemInfo
import com.thoughtworks.archguard.scanner.domain.system.SystemInfoRepository
import com.thoughtworks.archguard.scanner.infrastructure.client.AnalysisModuleClient
import com.thoughtworks.archguard.scanner.infrastructure.client.Scanner2Client
import com.thoughtworks.archguard.scanner.infrastructure.command.InMemoryConsumer
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.io.File
import java.nio.file.Path
import java.util.concurrent.CopyOnWriteArraySet
import java.util.concurrent.Executors
import javax.annotation.PostConstruct
import kotlin.io.path.createTempDirectory
import kotlin.io.path.pathString

@Service
class ArchitectureDependencyAnalysis(
    @Value("\${spring.datasource.url}") val dbUrl: String,
    @Value("\${spring.datasource.username}") val username: String,
    @Value("\${spring.datasource.password}") val password: String,
    @Value("\${init.scan.status}") val updateScanStatus: Boolean,
    val hubService: HubExecutorService,
    val systemInfoRepository: SystemInfoRepository,
    val analysisModuleClient: AnalysisModuleClient,
    val scanner2Client: Scanner2Client,
) {
    private val log = LoggerFactory.getLogger(ArchitectureDependencyAnalysis::class.java)
    private val runningSystemIdSet = CopyOnWriteArraySet<Long>()
    private val executor = Executors.newFixedThreadPool(5)

    @PostConstruct
    fun postConstruct() {
        if (updateScanStatus) {
            systemInfoRepository.updateScanningSystemToScanFail()
        }
    }

    fun asyncAnalyse(systemId: Long, scannerVersion: String) {
        if (runningSystemIdSet.contains(systemId)) {
            throw AnalysisException("this system is scanning: $systemId")
        }

        systemInfoRepository.removeNotClearRelatedData(systemId)
        executor.execute {
            val systemInfo = getSystemInfo(systemId)
            try {
                val workdir = createWorkingDirectoryIfNotExist(systemInfo)

                startScanSystem(systemInfo)
                analyse(systemId, scannerVersion, workdir)
            } catch (e: Exception) {
                log.error("Exception in asyncAnalyse: {}", e)
                stopScanSystem(systemInfo, ScannedType.FAILED)
            }
        }
    }

    private fun createWorkingDirectoryIfNotExist(systemInfo: SystemInfo): Path {
        val workdir: Path
        if (systemInfo.workdir.isEmpty()) {
            workdir = createTempDirectory("archguard")
            systemInfoRepository.setSystemWorkspace(systemInfo.id!!, workdir.pathString)
        } else if (!File(systemInfo.workdir).exists()) {
            workdir = createTempDirectory("archguard")
            systemInfoRepository.setSystemWorkspace(systemInfo.id!!, workdir.pathString)
        } else {
            workdir = File(systemInfo.workdir).toPath()
        }

        return workdir
    }

    fun analyse(systemId: Long, scannerVersion: String, workdir: Path) {
        val memoryConsumer = InMemoryConsumer()

        log.info("************************************")
        log.info(" Start scan analysis")
        log.info("************************************")
        val url = dbUrl.replace("://", "://$username:$password@")

        hubService.doScanIfNotRunning(systemId, url, scannerVersion, memoryConsumer)
        File(workdir.resolve("archguard.log").toString()).writeText(memoryConsumer.toString())
    }

    fun postAnalyse(systemId: Long, scannerVersion: String) {
        executor.execute {
            val systemInfo = getSystemInfo(systemId)
            try {
                val workdir = createWorkingDirectoryIfNotExist(systemInfo)
                postMetrics(systemId, workdir)

                stopScanSystem(systemInfo, ScannedType.SCANNED)
            } catch (e: Exception) {
                log.error("Exception in asyncAnalyse: {}", e)
                stopScanSystem(systemInfo, ScannedType.FAILED)
            }
        }
    }

    fun postMetrics(systemId: Long, workdir: Path) {
        val memoryConsumer = InMemoryConsumer()

        log.info("************************************")
        log.info(" Finished level 1 scanners")
        log.info("************************************")

        scanner2Client.level2MetricsAnalysis(systemId)
        log.info("************************************")
        log.info(" Finished level 2 analysis metrics")
        log.info("************************************")

        // todo: split to another api
        analysisModuleClient.autoDefine(systemId)
        log.info("************************************")
        log.info(" Finished logic module auto define")
        log.info("************************************")

        analysisModuleClient.badSmellDashboard(systemId)
        log.info("************************************")
        log.info(" Finished bad smell dashboard")
        log.info("************************************")

        try {
            File(workdir.resolve("archguard.log").toString()).writeText(memoryConsumer.toString())
        } catch (e: Exception) {
            log.info(e.toString())
        }
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

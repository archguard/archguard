package com.thoughtworks.archguard.aac

import com.thoughtworks.archguard.aac.model.AsCodeResponse
import com.thoughtworks.archguard.aac.model.PlaceHolder
import com.thoughtworks.archguard.aac.model.RepoStatus
import com.thoughtworks.archguard.scanner.domain.scanner.javaext.bs.ScanContext
import com.thoughtworks.archguard.scanner.domain.system.BuildTool
import com.thoughtworks.archguard.scanner.infrastructure.command.InMemoryConsumer
import com.thoughtworks.archguard.smartscanner.StranglerScannerExecutor
import com.thoughtworks.archguard.system_info.controller.SystemInfoDTO
import com.thoughtworks.archguard.system_info.controller.SystemInfoMapper
import com.thoughtworks.archguard.system_info.domain.SystemInfoService
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.io.File

class AsCodeRepositoryDTO(val name: String, val language: String, val scmUrl: String)
class AsCodeScanDTO(
    val name: String,
    val features: List<String>,
    val branch: String,
    val languages: List<String>,
    val specs: List<String>,
)

// todo: thinking in move to websocket?
@RestController
@RequestMapping("/api/ascode")
class AsCodeController(
    val systemInfoService: SystemInfoService,
    val systemInfoMapper: SystemInfoMapper,
    val executor: StranglerScannerExecutor,
) {
    private val logger = LoggerFactory.getLogger(AsCodeController::class.java)

    @PutMapping("/repos")
    fun createRepos(@RequestBody repos: List<AsCodeRepositoryDTO>): AsCodeResponse {
        val successes = mutableListOf<String>()
        val exists = mutableListOf<String>()

        repos.forEach {
            val systemInfoDTO = SystemInfoDTO(
                systemName = it.name,
                repo = listOf(it.scmUrl),
                language = it.language,
                badSmellThresholdSuiteId = 1
            )
            val systemInfo = systemInfoMapper.fromDTO(systemInfoDTO)
            try {
                systemInfoService.addSystemInfo(systemInfo)
                successes += it.name
            } catch (e: Exception) {
                exists += it.name
            }
        }

        return AsCodeResponse(
            content = RepoStatus(successes, exists)
        )
    }

    @PutMapping("/scan")
    fun createScan(
        @RequestBody scanInfo: AsCodeScanDTO,
        @RequestParam(defaultValue = "1.6.2") scannerVersion: String,
    ): AsCodeResponse {
        val systemInfo = systemInfoService.getSystemInfoByName(scanInfo.name)
        val memoryConsumer = InMemoryConsumer()

        val scanContext = ScanContext(
            systemId = systemInfo.id!!,
            repo = systemInfo.repo,
            buildTool = BuildTool.NONE,
            workspace = File(systemInfo.workdir!!),
            dbUrl = "",
            config = listOf(),
            language = systemInfo.language!!,
            codePath = systemInfo.codePath!!,
            branch = systemInfo.branch!!,
            logStream = memoryConsumer,
            scannerVersion = scannerVersion,
            additionArguments = listOf()
        );

        executor.execute(scanContext)

        return AsCodeResponse(
            content = PlaceHolder()
        )
    }
}

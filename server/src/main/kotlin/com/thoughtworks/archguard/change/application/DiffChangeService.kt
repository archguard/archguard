package com.thoughtworks.archguard.change.application

import com.thoughtworks.archguard.change.domain.DiffChange
import com.thoughtworks.archguard.change.domain.DiffChangeRepo
import com.thoughtworks.archguard.scanner.domain.scanner.javaext.bs.ScanContext
import com.thoughtworks.archguard.scanner.domain.system.BuildTool
import com.thoughtworks.archguard.scanner.infrastructure.command.InMemoryConsumer
import com.thoughtworks.archguard.smartscanner.StranglerScannerExecutor
import com.thoughtworks.archguard.system_info.domain.SystemInfo
import org.archguard.scanner.core.context.AnalyserType
import org.springframework.stereotype.Service
import java.io.File

@Service
class DiffChangeService(
    val diffChangeRepo: DiffChangeRepo,
    val scannerExecutor: StranglerScannerExecutor,
) {
    fun execute(systemInfo: SystemInfo, since: String, until: String, scannerVersion: String) {
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
            additionArguments = listOf(
                "--since=$since",
                "--until=$until",
            ),
            scannerVersion = scannerVersion
        )

        scannerExecutor.run(scanContext, AnalyserType.DIFF_CHANGES)
    }

    fun findBySystemId(id: Long): List<DiffChange> {
        return diffChangeRepo.findBySystemId(id)
    }
}

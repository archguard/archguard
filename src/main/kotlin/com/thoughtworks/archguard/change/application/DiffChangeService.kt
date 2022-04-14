package com.thoughtworks.archguard.change.application

import com.thoughtworks.archguard.change.domain.DiffChange
import com.thoughtworks.archguard.change.domain.DiffChangeRepo
import com.thoughtworks.archguard.scanner.domain.scanner.diff.DiffChangesScanner
import com.thoughtworks.archguard.scanner.domain.scanner.javaext.bs.ScanContext
import com.thoughtworks.archguard.scanner.domain.system.BuildTool
import com.thoughtworks.archguard.scanner.infrastructure.command.InMemoryConsumer
import com.thoughtworks.archguard.system_info.domain.SystemInfo
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.io.File

@Service
class DiffChangeService(
    @Value("\${spring.datasource.url}") val dbUrl: String,
    @Value("\${spring.datasource.username}") val username: String,
    @Value("\${spring.datasource.password}") val password: String,
    val changeScanner: DiffChangesScanner,
    val diffChangeRepo: DiffChangeRepo
) {
    val url = dbUrl.replace("://", "://$username:$password@")

    fun execute(systemInfo: SystemInfo, since: String, until: String,) {
        val memoryConsumer = InMemoryConsumer()
        val scanContext = ScanContext(
            systemId = systemInfo.id!!,
            repo = systemInfo.repo,
            buildTool = BuildTool.NONE,
            workspace = File(systemInfo.workdir!!),
            dbUrl = url,
            config = listOf(),
            language = systemInfo.language!!,
            codePath = systemInfo.codePath!!,
            branch = systemInfo.branch!!,
            logStream = memoryConsumer,
            additionArguments = listOf(
                "--since=$since",
                "--until=$until",
            )
        )

        changeScanner.scan(scanContext)
    }

    fun findBySystemId(id: Long): List<DiffChange> {
        return diffChangeRepo.findBySystemId(id)
    }
}

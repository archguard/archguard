package com.thoughtworks.archguard.scanner.domain

import com.thoughtworks.archguard.scanner.domain.config.model.ToolConfigure
import com.thoughtworks.archguard.scanner.domain.system.BuildTool
import java.io.File

data class ScanContext(val systemId: Long, var repo: String, var buildTool: BuildTool, val workspace: File, val dbUrl: String, val config: List<ToolConfigure>) {

    fun canScan(scanTool: String): Boolean {

        val available = config.find { it.type.equals(scanTool) }?.configs?.get("available")
        return "true".equals(available)
    }
}

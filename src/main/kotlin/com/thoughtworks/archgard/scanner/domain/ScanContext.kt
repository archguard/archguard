package com.thoughtworks.archgard.scanner.domain

import com.thoughtworks.archgard.scanner.domain.config.model.ToolConfigure
import com.thoughtworks.archgard.scanner.domain.system.BuildTool
import java.io.File

data class ScanContext(val systemId: Long, var repo: String, var buildTool: BuildTool, val workspace: File, val config: List<ToolConfigure>) {

    fun canScan(scanTool: String): Boolean {
        val available = config.find { it.type.equals(scanTool) }?.configs?.get("available")
        return "true".equals(available)
    }
}

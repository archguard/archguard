package com.thoughtworks.archgard.scanner.domain.scanner

import com.thoughtworks.archgard.scanner.domain.ScanContext
import com.thoughtworks.archgard.scanner.domain.config.model.ToolConfigure

interface Scanner {
    val toolList: List<ToolConfigure>
        get() = toolListGenerator()

    fun getScannerName(): String

    fun toolListGenerator(): List<ToolConfigure>

    fun scan(context: ScanContext)

    fun canScan(context: ScanContext): Boolean {
        return context.canScan(getScannerName())
    }
}
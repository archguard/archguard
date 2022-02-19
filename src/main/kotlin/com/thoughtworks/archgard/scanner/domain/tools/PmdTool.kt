package com.thoughtworks.archgard.scanner.domain.tools

import com.thoughtworks.archgard.scanner.domain.ScanContext
import java.io.File

class PmdTool(private val context: ScanContext) {

    fun getReportFiles(): List<File> {
        val configs = context.config.find { it.type == "pmd" }?.configs
        val reportFile = configs?.get("reportFile")?.split(',')
        return reportFile?.map { File(it) }?.filter { it.exists() } ?: emptyList()
    }

}
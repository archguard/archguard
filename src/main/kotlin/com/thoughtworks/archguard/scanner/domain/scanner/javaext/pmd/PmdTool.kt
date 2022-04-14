package com.thoughtworks.archguard.scanner.domain.scanner.javaext.pmd

import com.thoughtworks.archguard.scanner.domain.scanner.javaext.bs.ScanContext
import java.io.File

class PmdTool(private val context: ScanContext) {

    fun getReportFiles(): List<File> {
        val configs = context.config.find { it.type == "pmd" }?.configs
        val reportFile = configs?.get("reportFile")?.split(',')
        return reportFile?.map { File(it) }?.filter { it.exists() } ?: emptyList()
    }
}

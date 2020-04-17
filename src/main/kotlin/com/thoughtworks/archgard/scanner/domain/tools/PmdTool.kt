package com.thoughtworks.archgard.scanner.domain.tools

import com.thoughtworks.archgard.scanner.domain.ScanContext
import java.io.File

class PmdTool(private val context: ScanContext) {

    fun getReportFiles(): List<File> {
        val reportFile = context.config.groupBy { it.type }.getValue("pmd").filter { it.key == "report" }.map { it.value }
        return reportFile.map { File(it) }.filter { it.exists() }
    }

}
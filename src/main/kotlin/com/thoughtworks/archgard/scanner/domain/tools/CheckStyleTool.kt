package com.thoughtworks.archgard.scanner.domain.tools

import com.thoughtworks.archgard.scanner.domain.ScanContext
import java.io.File

class CheckStyleTool(private val context: ScanContext) : StyleReport {

    override fun getStyleReport(): List<File> {
        val reportFile = context.config.groupBy { it.type }.getValue("checkstyle").filter { it.key == "report" }.map { it.value }
        return reportFile.map { File(it) }.filter { it.exists() }
    }

}
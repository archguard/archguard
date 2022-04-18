package com.thoughtworks.archguard.scanner.domain.scanner.javaext.checkstyle

import com.thoughtworks.archguard.scanner.domain.scanner.javaext.bs.ScanContext
import com.thoughtworks.archguard.scanner.domain.tools.StyleReport
import java.io.File

class CheckStyleTool(private val context: ScanContext) : StyleReport {
        val configs = context.config.find { it.type == "checkstyle" }?.configs
        val reportFile = configs?.get("reportFile")?.split(',')
        return reportFile?.map { File(it) }?.filter { it.exists() } ?: emptyList()
    }
}

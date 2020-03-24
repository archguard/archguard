package com.thoughtworks.archgard.scanner.domain.scanner.dependencies

import com.thoughtworks.archgard.scanner.domain.ScanContext
import com.thoughtworks.archgard.scanner.domain.scanner.Scanner
import com.thoughtworks.archgard.scanner.domain.tools.JavaByteCodeTool
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class JavaDependencyScanner(@Value("spring.datasource.url") val dbUrl: String) : Scanner {

    override fun scan(context: ScanContext) {
        val javaByteCodeTool = JavaByteCodeTool(context.workspace, dbUrl)
        javaByteCodeTool.getDependencyReport()
    }

}
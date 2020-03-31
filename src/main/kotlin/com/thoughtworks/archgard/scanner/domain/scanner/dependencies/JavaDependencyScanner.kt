package com.thoughtworks.archgard.scanner.domain.scanner.dependencies

import com.thoughtworks.archgard.scanner.domain.ScanContext
import com.thoughtworks.archgard.scanner.domain.scanner.Scanner
import com.thoughtworks.archgard.scanner.domain.tools.JavaByteCodeTool
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class JavaDependencyScanner(@Value("spring.datasource.url") val dbUrl: String) : Scanner {
    private val log = LoggerFactory.getLogger(JavaDependencyScanner::class.java)
    override val name: String = "JavaDependency"

    override fun scan(context: ScanContext) {
        log.info("start scan java dependency")
        val javaByteCodeTool = JavaByteCodeTool(context.workspace, dbUrl)
        javaByteCodeTool.getDependencyReport()
        log.info("finished scan java dependency")
    }

}
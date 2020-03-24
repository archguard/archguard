package com.thoughtworks.archgard.scanner.domain.dependencies

import com.thoughtworks.archgard.scanner.domain.ScanContext
import com.thoughtworks.archgard.scanner.domain.Scanner
import com.thoughtworks.archgard.scanner.domain.toolscanners.JavaByteCodeScanner
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class JavaDependencyScanner(@Value("spring.datasource.url") val dbUrl: String) : Scanner {

    override fun scan(context: ScanContext) {
        val javaByteCodeScanner = JavaByteCodeScanner(context.workspace, dbUrl)
        javaByteCodeScanner.getDependencyReport()
    }

}
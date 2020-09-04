package com.thoughtworks.archgard.scanner.domain.scanner.bytecode

import com.thoughtworks.archgard.scanner.domain.ScanContext
import com.thoughtworks.archgard.scanner.domain.scanner.Scanner
import com.thoughtworks.archgard.scanner.domain.tools.JavaByteCodeTool
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class ByteCodeScanner() : Scanner {
    private val log = LoggerFactory.getLogger(ByteCodeScanner::class.java)

    override fun getScannerName(): String {
        return "Byte Code"
    }

    override fun scan(context: ScanContext) {
        val javaByteCodeTool = JavaByteCodeTool(context.workspace, context.dbUrl, context.systemId)
        javaByteCodeTool.analyse()
        log.info("finished scan java byte code")
    }
}
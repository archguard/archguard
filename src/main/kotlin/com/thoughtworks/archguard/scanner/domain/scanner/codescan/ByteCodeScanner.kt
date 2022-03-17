package com.thoughtworks.archguard.scanner.domain.scanner.codescan

import com.thoughtworks.archguard.scanner.domain.ScanContext
import com.thoughtworks.archguard.scanner.domain.config.model.ToolConfigure
import com.thoughtworks.archguard.scanner.domain.scanner.Scanner
import com.thoughtworks.archguard.scanner.domain.tools.GitScannerTool
import com.thoughtworks.archguard.scanner.domain.tools.JavaByteCodeTool
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ByteCodeScanner(@Autowired val sourceCodeScanRepo: SourceCodeScanRepo) : Scanner {
    private val log = LoggerFactory.getLogger(ByteCodeScanner::class.java)

    override fun getScannerName(): String {
        return "Byte Code"
    }

    override fun canScan(context: ScanContext): Boolean {
        return context.language == "jvm"
    }

    override val toolList: List<ToolConfigure>
        get() = ArrayList()

    override fun scan(context: ScanContext) {
        scanByteCode(context)
        scanLoc(context)
    }

    private fun scanByteCode(context: ScanContext) {
        val javaByteCodeTool = JavaByteCodeTool(context.workspace, context.dbUrl, context.systemId)
        javaByteCodeTool.analyse()
        log.info("finished scan java byte code")
    }

    private fun scanLoc(context: ScanContext) {
        log.info("start update loc")
        val gitScannerTool = GitScannerTool(context.workspace, null, context.systemId, context.repo)
        val locReport = gitScannerTool.getLocReport()
        if (locReport != null) {
            sourceCodeScanRepo.updateJClassLoc(locReport)
            log.info("finished scan loc source")
        } else {
            log.warn("failed to scan loc")
        }
    }
}
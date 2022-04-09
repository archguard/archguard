package com.thoughtworks.archguard.scanner.domain.scanner.codescan.bytecode

import com.thoughtworks.archguard.scanner.domain.scanner.javaext.bs.ScanContext
import com.thoughtworks.archguard.scanner.domain.config.model.ToolConfigure
import com.thoughtworks.archguard.scanner.domain.scanner.Scanner
import com.thoughtworks.archguard.scanner.domain.scanner.git.GitScannerTool
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ByteCodeScanner(@Autowired val byteCodeScanRepo: ByteCodeScanRepo) : Scanner {
    private val log = LoggerFactory.getLogger(ByteCodeScanner::class.java)

    override fun getScannerName(): String {
        return "Byte Code"
    }

    override fun canScan(context: ScanContext): Boolean {
        return context.language.lowercase() == "jvm"
    }

    override val toolList: List<ToolConfigure>
        get() = ArrayList()

    override fun scan(context: ScanContext) {
        scanByteCode(context)
        scanLoc(context)
    }

    private fun scanByteCode(context: ScanContext) {
        val byteCodeTool = ByteCodeTool(context.workspace, context.dbUrl, context.systemId, context.logStream)
        byteCodeTool.analyse()
        log.info("finished scan java byte code")
    }

    private fun scanLoc(context: ScanContext) {
        log.info("start update loc")
        val gitScannerTool = GitScannerTool(context.workspace, null, context.systemId, context.repo, context.logStream)
        val locReport = gitScannerTool.getLocReport()
        if (locReport != null) {
            byteCodeScanRepo.updateJClassLoc(locReport)
            log.info("finished scan loc source")
        } else {
            log.warn("failed to scan loc")
        }
    }
}
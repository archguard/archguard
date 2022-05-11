package com.thoughtworks.archguard.scanner.domain.scanner.javaext.bs

import com.thoughtworks.archguard.scanner.domain.scanner.Scanner
import com.thoughtworks.archguard.scanner.domain.tools.DesigniteJavaReportType
import com.thoughtworks.archguard.scanner.domain.tools.DesigniteJavaTool
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.UUID

@Deprecated("Deprecated features, will be removed in future")
@Service
class BadSmellScanner(@Autowired val badSmellRepo: BadSmellRepo) : Scanner {

    private val log = LoggerFactory.getLogger(BadSmellScanner::class.java)
    override fun getScannerName(): String {
        return "BadSmell"
    }

    override fun canScan(context: ScanContext): Boolean {
        return context.language.lowercase() == "jvm"
    }

    override fun scan(context: ScanContext) {
        log.info("start scan bad smell report")
        val badSmell = getDesigniteJavaBadSmell(context)
        badSmellRepo.save(badSmell)

        log.info("finished scan bad smell report")
    }

    private fun getDesigniteJavaBadSmell(context: ScanContext): List<BadSmell> {
        val designiteJavaTool = DesigniteJavaTool(context.workspace, context.logStream)
        return designiteJavaTool.readReport(DesigniteJavaReportType.BAD_SMELL_METRICS).map {
            val elements = it.split(",")
            BadSmell(
                UUID.randomUUID().toString(), context.systemId, elements[1] + "." + elements[2],
                0, elements[3], 0, elements[3]
            )
        }
    }
}

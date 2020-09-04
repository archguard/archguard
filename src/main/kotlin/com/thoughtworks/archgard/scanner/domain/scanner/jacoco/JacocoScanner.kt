package com.thoughtworks.archgard.scanner.domain.scanner.jacoco

import com.thoughtworks.archgard.scanner.domain.ScanContext
import com.thoughtworks.archgard.scanner.domain.config.model.ToolConfigure
import com.thoughtworks.archgard.scanner.domain.scanner.Scanner
import com.thoughtworks.archgard.scanner.domain.tools.JacocoTool
import com.thoughtworks.archgard.scanner.infrastructure.db.SqlScriptRunner
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.io.File

@Component
class JacocoScanner(@Autowired val sqlScriptRunner: SqlScriptRunner) : Scanner {
    private val DELETE_BUNDLE = "delete from bundle where 1=1"
    private val DELETE_ITEM = "delete from item where 1=1"

    private val log = LoggerFactory.getLogger(JacocoScanner::class.java)
    override fun getScannerName(): String {
        return "Jacoco"
    }

    override fun scan(context: ScanContext) {
        log.info("start scan jacoco exec file")
        sqlScriptRunner.run(DELETE_BUNDLE)
        sqlScriptRunner.run(DELETE_ITEM)
        getTargetProjects(context.workspace)
                .forEach { w -> runSql(JacocoTool(context.workspace, w, context.buildTool).execToSql()) }
        log.info("finished scan jacoco exec file")
    }

    private fun runSql(sqlFile: File?) {
        if (sqlFile != null) {
            sqlScriptRunner.run(sqlFile)
        }
    }

    private fun getTargetProjects(workspace: File): List<File> {
        return workspace.walkTopDown()
                .filter { f -> f.absolutePath.endsWith("jacoco.exec") }
                .map { f -> f.parentFile.parentFile }
                .toList()
    }
}
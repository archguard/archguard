package com.thoughtworks.archgard.scanner.domain.scanner.jacoco

import com.thoughtworks.archgard.scanner.domain.ScanContext
import com.thoughtworks.archgard.scanner.domain.config.model.ToolConfigure
import com.thoughtworks.archgard.scanner.domain.scanner.Scanner
import com.thoughtworks.archgard.scanner.domain.tools.JacocoTool
import com.thoughtworks.archgard.scanner.infrastructure.db.SqlScriptRunner
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class JacocoScanner(@Autowired val sqlScriptRunner: SqlScriptRunner) : Scanner {
    private val DELETE_BUNDLE = "delete from bundle where 1=1"
    private val DELETE_ITEM = "delete from item where 1=1"

    private val log = LoggerFactory.getLogger(JacocoScanner::class.java)
    override fun toolListGenerator(): List<ToolConfigure> {
        val result = ArrayList<ToolConfigure>()
        val config = HashMap<String, String>()
        config["available"] = "false"
        result.add(ToolConfigure("Jacoco", config))
        return result
    }

    override fun scan(context: ScanContext) {
        log.info("start scan Jacoco source")
        val jacocoTool = JacocoTool(context.workspace)
        val jacocoSql = jacocoTool.execToSql()
        if (jacocoSql != null) {
            sqlScriptRunner.run(DELETE_BUNDLE)
            sqlScriptRunner.run(DELETE_ITEM)
            sqlScriptRunner.run(jacocoSql)
        }
        log.info("finished scan git source")
    }

}
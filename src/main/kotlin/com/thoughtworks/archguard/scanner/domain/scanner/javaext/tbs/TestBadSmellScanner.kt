package com.thoughtworks.archguard.scanner.domain.scanner.javaext.tbs

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.thoughtworks.archguard.scanner.domain.scanner.Scanner
import com.thoughtworks.archguard.scanner.domain.scanner.javaext.bs.ScanContext
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class TestBadSmellScanner(@Autowired val testBadSmellRepo: TestBadSmellRepo) : Scanner {
    private val log = LoggerFactory.getLogger(TestBadSmellScanner::class.java)

    private val mapper = jacksonObjectMapper()
    override fun getScannerName(): String {
        return "TestBadSmell"
    }

    override fun canScan(context: ScanContext): Boolean {
        return context.language.lowercase() == "jvm" ||
            context.language.lowercase() == "java" ||
            context.language.lowercase() == "kotlin"
    }

    override fun scan(context: ScanContext) {
        log.info("start scan test bad smell")
        val coca = TestBadsmellTool(context.workspace, context.logStream, context.scannerVersion)
        val report = coca.getTestBadSmellReport()
        val model = mapper.readValue<List<CocaTestBadSmellModel>>(report?.readText() ?: "[]")
        val testBadSmells = model
            .map { m -> TestBadSmell(UUID.randomUUID().toString(), context.systemId, m.line, m.fileName, m.description, m.type) }
        testBadSmellRepo.save(testBadSmells)

        val shellTool = ShellTool(context.workspace, context.logStream)
        val countTest = shellTool.countTest()
        testBadSmellRepo.saveCount(Integer.parseInt(countTest.readText().trim()))
        log.info("finished scan test bad smell")
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class CocaTestBadSmellModel(val fileName: String, val type: String, val description: String, val line: Int)
}

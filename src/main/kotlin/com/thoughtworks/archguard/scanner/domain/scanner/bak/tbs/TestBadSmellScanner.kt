package com.thoughtworks.archguard.scanner.domain.scanner.bak.tbs

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.thoughtworks.archguard.scanner.domain.ScanContext
import com.thoughtworks.archguard.scanner.domain.scanner.Scanner
import com.thoughtworks.archguard.scanner.domain.tools.CocaTool
import com.thoughtworks.archguard.scanner.domain.tools.ShellTool
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class TestBadSmellScanner(@Autowired val testBadSmellRepo: TestBadSmellRepo) : Scanner {
    private val log = LoggerFactory.getLogger(TestBadSmellScanner::class.java)

    private val mapper = jacksonObjectMapper()
    override fun getScannerName(): String {
        return "TestBadSmell"
    }

    override fun scan(context: ScanContext) {
        log.info("start scan test bad smell")
        val coca = CocaTool(context.workspace)
        val report = coca.getTestBadSmellReport()
        val model = mapper.readValue<List<CocaTestBadSmellModel>>(report?.readText() ?: "[]")
        val testBadSmells = model
                .map { m -> TestBadSmell(UUID.randomUUID().toString(), context.systemId, m.Line, m.FileName, m.Description, m.Type) }
        testBadSmellRepo.save(testBadSmells)

        val shellTool = ShellTool(context.workspace)
        val countTest = shellTool.countTest()
        testBadSmellRepo.saveCount(Integer.parseInt(countTest.readText().trim()))
        log.info("finished scan test bad smell")
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class CocaTestBadSmellModel(val FileName: String, val Type: String, val Description: String, val Line: Int)

}
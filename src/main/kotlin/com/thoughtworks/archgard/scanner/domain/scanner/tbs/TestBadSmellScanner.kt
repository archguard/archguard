package com.thoughtworks.archgard.scanner.domain.scanner.tbs

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.thoughtworks.archgard.scanner.domain.ScanContext
import com.thoughtworks.archgard.scanner.domain.config.model.ToolConfigure
import com.thoughtworks.archgard.scanner.domain.scanner.Scanner
import com.thoughtworks.archgard.scanner.domain.tools.CocaTool
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class TestBadSmellScanner(@Autowired val testBadSmellRepo: TestBadSmellRepo) : Scanner {
    private val log = LoggerFactory.getLogger(TestBadSmellScanner::class.java)

    private val mapper = jacksonObjectMapper()
    override fun toolListGenerator(): List<ToolConfigure> {
        val result = ArrayList<ToolConfigure>()
        val config = HashMap<String, String>()
        config["available"] = "false"
        result.add(ToolConfigure("TestBadSmell", config))
        return result
    }

    override fun scan(context: ScanContext) {
        log.info("start scan test bad smell")
        val coca = CocaTool(context.workspace)
        val report = coca.getTestBadSmellReport()
        val model = mapper.readValue<List<CocaTestBadSmellModel>>(report?.readText() ?: "[]")
        val testBadSmells = model
                .map { m -> TestBadSmell(UUID.randomUUID().toString(), m.Line, m.FileName, m.Description, m.Type) }
        testBadSmellRepo.save(testBadSmells)
        log.info("finished scan test bad smell")
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class CocaTestBadSmellModel(val FileName: String, val Type: String, val Description: String, val Line: Int)

}
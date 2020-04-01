package com.thoughtworks.archgard.scanner.domain.scanner.bs

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.thoughtworks.archgard.scanner.domain.ScanContext
import com.thoughtworks.archgard.scanner.domain.config.model.ToolConfigure
import com.thoughtworks.archgard.scanner.domain.scanner.Scanner
import com.thoughtworks.archgard.scanner.domain.tools.CocaTool
import com.thoughtworks.archgard.scanner.domain.tools.DesigniteJavaTool
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class BadSmellScanner(@Autowired val badSmellRepo: BadSmellRepo) : Scanner {

    private val log = LoggerFactory.getLogger(BadSmellScanner::class.java)

    private val mapper = jacksonObjectMapper()
    override fun toolListGenerator(): List<ToolConfigure> {
        val result = ArrayList<ToolConfigure>()
        val config = HashMap<String, String>()
        config["available"] = "false"
        result.add(ToolConfigure("BadSmell", config))
        return result
    }


    override fun scan(context: ScanContext) {
        log.info("start scan bad smell report")
        var badSmell = getCocaBadSmell(context)
        if (badSmell.isEmpty()) {
            badSmell = getDesigniteJavaBadSmell(context)
        }
        badSmellRepo.save(badSmell)

        log.info("finished scan bad smell report")
    }

    private fun getDesigniteJavaBadSmell(context: ScanContext): List<BadSmell> {
        val designiteJavaTool = DesigniteJavaTool(context.workspace)
        val badSmellReport = designiteJavaTool.getBadSmellReport()
        val lines = badSmellReport?.readLines()
        val readLines = lines?.subList(1, lines.size).orEmpty()

        val result = ArrayList<BadSmell>()
        for (line in readLines) {
            val elements = line.split(",")
            result.add(BadSmell(UUID.randomUUID().toString(), elements[1] + "." + elements[2], 0,
                    elements[3], 0, elements[3]))
        }
        return result
    }

    private fun getCocaBadSmell(context: ScanContext): List<BadSmell> {
        val cocaTool = CocaTool(context.workspace)
        val report = cocaTool.getBadSmellReport()
        val cocaModels = mapper.readValue<CocaBadSmellModel>(report?.readText() ?: "{}")
        return cocaModels.toBadSmell()
    }

    data class CocaBadSmellModel(val complexCondition: List<CocaBadSmellItem>?,
                                 val dataClass: List<CocaBadSmellItem>?,
                                 val graphConnectedCall: List<CocaBadSmellItem>?,
                                 val largeClass: List<CocaBadSmellItem>?,
                                 val lazyElement: List<CocaBadSmellItem>?,
                                 val longMethod: List<CocaBadSmellItem>?,
                                 val longParameterList: List<CocaBadSmellItem>?,
                                 val repeatedSwitches: List<CocaBadSmellItem>?) {
        fun toBadSmell(): List<BadSmell> {
            return listOf(complexCondition?.map { c -> BadSmell(UUID.randomUUID().toString(), c.EntityName, c.Line, c.Description, c.Size, "complexCondition") }
                    ?: emptyList(),
                    dataClass?.map { c -> BadSmell(UUID.randomUUID().toString(), c.EntityName, c.Line, c.Description, c.Size, "dataClass") }
                            ?: emptyList(),
                    graphConnectedCall?.map { c -> BadSmell(UUID.randomUUID().toString(), c.EntityName, c.Line, c.Description, c.Size, "graphConnectedCall") }
                            ?: emptyList(),
                    largeClass?.map { c -> BadSmell(UUID.randomUUID().toString(), c.EntityName, c.Line, c.Description, c.Size, "largeClass") }
                            ?: emptyList(),
                    lazyElement?.map { c -> BadSmell(UUID.randomUUID().toString(), c.EntityName, c.Line, c.Description, c.Size, "lazyElement") }
                            ?: emptyList(),
                    longMethod?.map { c -> BadSmell(UUID.randomUUID().toString(), c.EntityName, c.Line, c.Description, c.Size, "longMethod") }
                            ?: emptyList(),
                    longParameterList?.map { c -> BadSmell(UUID.randomUUID().toString(), c.EntityName, c.Line, c.Description, c.Size, "longParameterList") }
                            ?: emptyList(),
                    repeatedSwitches?.map { c -> BadSmell(UUID.randomUUID().toString(), c.EntityName, c.Line, c.Description, c.Size, "repeatedSwitches") }
                            ?: emptyList()
            ).flatten()
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class CocaBadSmellItem(val EntityName: String, val Line: Int, val Description: String, val Size: Int)

}
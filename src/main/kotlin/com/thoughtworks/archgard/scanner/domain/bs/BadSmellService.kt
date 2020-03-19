package com.thoughtworks.archgard.scanner.domain.bs

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.thoughtworks.archgard.scanner.domain.ScanContext
import com.thoughtworks.archgard.scanner.domain.toolscanners.CocaScanner
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class BadSmellService {
    @Autowired
    lateinit var badSmellRepo: BadSmellRepo

    private val mapper = jacksonObjectMapper()

    fun scan(context: ScanContext) {
        val cocaScanner = CocaScanner("http://ci.archguard.org/view/ThirdPartyTool/job/coca/lastSuccessfulBuild/artifact/coca",
                context.projectRoot)
        val report = cocaScanner.getBadSmellReport()
        val badSmell = mapper.readValue<CocaBadSmellModel>(report).toBadSmell()
        badSmellRepo.save(badSmell)
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
            return listOf(complexCondition?.map { c -> BadSmell(c.EntityName, c.Line, c.Description, c.Size, "complexCondition") }
                    ?: emptyList(),
                    dataClass?.map { c -> BadSmell(c.EntityName, c.Line, c.Description, c.Size, "dataClass") }
                            ?: emptyList(),
                    graphConnectedCall?.map { c -> BadSmell(c.EntityName, c.Line, c.Description, c.Size, "graphConnectedCall") }
                            ?: emptyList(),
                    largeClass?.map { c -> BadSmell(c.EntityName, c.Line, c.Description, c.Size, "largeClass") }
                            ?: emptyList(),
                    lazyElement?.map { c -> BadSmell(c.EntityName, c.Line, c.Description, c.Size, "lazyElement") }
                            ?: emptyList(),
                    longMethod?.map { c -> BadSmell(c.EntityName, c.Line, c.Description, c.Size, "longMethod") }
                            ?: emptyList(),
                    longParameterList?.map { c -> BadSmell(c.EntityName, c.Line, c.Description, c.Size, "longParameterList") }
                            ?: emptyList(),
                    repeatedSwitches?.map { c -> BadSmell(c.EntityName, c.Line, c.Description, c.Size, "repeatedSwitches") }
                            ?: emptyList()
            ).flatten()
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class CocaBadSmellItem(val EntityName: String, val Line: Int, val Description: String, val Size: Int)

}
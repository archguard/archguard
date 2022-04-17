import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.thoughtworks.archguard.scanner.domain.scanner.Scanner
import com.thoughtworks.archguard.scanner.domain.tools.DesigniteJavaReportType
import com.thoughtworks.archguard.scanner.domain.tools.DesigniteJavaTool
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.UUID

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

    data class CocaBadSmellModel(
        val complexCondition: List<CocaBadSmellItem>?,
        val dataClass: List<CocaBadSmellItem>?,
        val graphConnectedCall: List<CocaBadSmellItem>?,
        val largeClass: List<CocaBadSmellItem>?,
        val lazyElement: List<CocaBadSmellItem>?,
        val longMethod: List<CocaBadSmellItem>?,
        val longParameterList: List<CocaBadSmellItem>?,
        val repeatedSwitches: List<CocaBadSmellItem>?
    ) {
        fun toBadSmell(systemId: Long): List<BadSmell> {
            return listOf(
                complexCondition?.map { c -> BadSmell(UUID.randomUUID().toString(), systemId, c.EntityName, c.Line, c.Description, c.Size, "complexCondition") }
                    ?: emptyList(),
                dataClass?.map { c -> BadSmell(UUID.randomUUID().toString(), systemId, c.EntityName, c.Line, c.Description, c.Size, "dataClass") }
                    ?: emptyList(),
                graphConnectedCall?.map { c -> BadSmell(UUID.randomUUID().toString(), systemId, c.EntityName, c.Line, c.Description, c.Size, "graphConnectedCall") }
                    ?: emptyList(),
                largeClass?.map { c -> BadSmell(UUID.randomUUID().toString(), systemId, c.EntityName, c.Line, c.Description, c.Size, "largeClass") }
                    ?: emptyList(),
                lazyElement?.map { c -> BadSmell(UUID.randomUUID().toString(), systemId, c.EntityName, c.Line, c.Description, c.Size, "lazyElement") }
                    ?: emptyList(),
                longMethod?.map { c -> BadSmell(UUID.randomUUID().toString(), systemId, c.EntityName, c.Line, c.Description, c.Size, "longMethod") }
                    ?: emptyList(),
                longParameterList?.map { c -> BadSmell(UUID.randomUUID().toString(), systemId, c.EntityName, c.Line, c.Description, c.Size, "longParameterList") }
                    ?: emptyList(),
                repeatedSwitches?.map { c -> BadSmell(UUID.randomUUID().toString(), systemId, c.EntityName, c.Line, c.Description, c.Size, "repeatedSwitches") }
                    ?: emptyList()
            ).flatten()
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class CocaBadSmellItem(val EntityName: String, val Line: Int, val Description: String?, val Size: Int)
}

package com.thoughtworks.archgard.scanner.domain.toolscanners

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.thoughtworks.archgard.scanner.domain.ScanContext
import com.thoughtworks.archgard.scanner.domain.bs.BadSmell
import com.thoughtworks.archgard.scanner.domain.bs.BadSmellRepo
import com.thoughtworks.archgard.scanner.infrastructure.FileDownloader
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.io.File
import java.io.IOException
import java.net.URL

@Component
class CocaScanner : ScannerLifecycle {
    private val latestCocaUrl = URL("https://github.com/phodal/coca/releases/download/v1.9.9-beta/coca_linux")
    private val mapper = jacksonObjectMapper()

    @Autowired
    private lateinit var badSmellRepo: BadSmellRepo

    override fun preProcess(context: ScanContext?) {
        FileDownloader.download(latestCocaUrl, File(context!!.projectRoot.toString() + "/coca"))
    }

    @Throws(IOException::class, InterruptedException::class)
    override fun scan(context: ScanContext?) {
        val badSmell = ProcessBuilder("./coca", "bs", "-s", "type")
        badSmell.directory(context?.projectRoot)
        badSmell.start().waitFor()
    }

    override fun storeReport(context: ScanContext?) {
        val badSmellReport = File(context!!.projectRoot.toString() + "/coca_reporter/bs.json").readText()
        val badSmell = mapper.readValue<CocaBadSmellModel>(badSmellReport)
        badSmellRepo.save(badSmell.toBadSmell())
    }

    override fun clean(context: ScanContext?) {
        File(context!!.projectRoot.toString() + "/coca").delete()
        File(context.projectRoot.toString() + "/coca_reporter/bs.json").delete()
    }

    data class CocaBadSmellModel(val complexCondition: List<CocaBadSmellItem>,
                                 val dataClass: List<CocaBadSmellItem>,
                                 val graphConnectedCall: List<CocaBadSmellItem>,
                                 val largeClass: List<CocaBadSmellItem>,
                                 val lazyElement: List<CocaBadSmellItem>,
                                 val longMethod: List<CocaBadSmellItem>,
                                 val longParameterList: List<CocaBadSmellItem>,
                                 val repeatedSwitches: List<CocaBadSmellItem>) {
        fun toBadSmell(): BadSmell {
            TODO("Not yet implemented")
        }
    }

    data class CocaBadSmellItem(val EntityName: String, val Line: String, val Description: String, val Size: String)
}
package com.thoughtworks.archgard.scanner.domain.scanner.checkstyle

import com.thoughtworks.archgard.scanner.domain.ScanContext
import com.thoughtworks.archgard.scanner.domain.scanner.Scanner
import com.thoughtworks.archgard.scanner.domain.scanner.dependencies.JavaDependencyScanner
import com.thoughtworks.archgard.scanner.domain.tools.CheckStyleTool
import org.dom4j.Element
import org.dom4j.io.SAXReader
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.io.File
import java.util.*

@Service
class CheckstyleScanner : Scanner {

    private val log = LoggerFactory.getLogger(JavaDependencyScanner::class.java)

    @Autowired
    private lateinit var checkStyleRepo: CheckStyleRepo

    override fun scan(context: ScanContext) {
        val styleReport = CheckStyleTool(context).getStyleReport()
        val checkStyles = styleReport.mapNotNull { mapTo(it) }.flatten()
        save(checkStyles)
    }

    private fun mapTo(file: File): List<CheckStyle>? {
        val saxReader = SAXReader()
        try {
            val document = saxReader.read(file)
            val rootElement = document.rootElement
            if (rootElement.name == "checkstyle") {
                val list = rootElement.elements().map { e ->
                    e as Element
                    val name = e.attributeValue("name")
                    e.elements().map {
                        val element = it as Element
                        CheckStyle(UUID.randomUUID().toString(), name,
                                element.attributeValue("source"),
                                element.attributeValue("message"),
                                element.attributeValue("line").toInt(),
                                element.attributeValue("column").orEmpty().toIntOrNull() ?: 0,
                                element.attributeValue("severity"))
                    }
                }
                return list.flatten()
            }
        } catch (e: Exception) {
            log.error("failed to parse checkstyle ", e)
        }
        return null
    }

    private fun save(reports: List<CheckStyle>) {
        checkStyleRepo.deleteAll()
        checkStyleRepo.save(reports)
    }
}
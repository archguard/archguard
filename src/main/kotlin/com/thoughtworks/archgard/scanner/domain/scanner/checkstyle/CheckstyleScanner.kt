package com.thoughtworks.archgard.scanner.domain.scanner.checkstyle

import com.thoughtworks.archgard.scanner.domain.ScanContext
import com.thoughtworks.archgard.scanner.domain.scanner.Scanner
import com.thoughtworks.archgard.scanner.domain.scanner.dependencies.JavaDependencyScanner
import org.dom4j.Element
import org.dom4j.io.SAXReader
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

@Service
class CheckstyleScanner : Scanner {

    private val log = LoggerFactory.getLogger(JavaDependencyScanner::class.java)

    @Autowired
    private lateinit var checkStyleRepo: CheckStyleRepo

    override fun scan(context: ScanContext) {
        val reportFile = context.config["checkStyleLocation"] as ArrayList<String>
        if (reportFile.isNotEmpty()) {
            val report = analysis(reportFile)
            save(report)
        }
    }

    private fun analysis(reportFile: ArrayList<String>): ArrayList<CheckStyle> {
        val reports = ArrayList<CheckStyle>()
        reportFile.filter { File(it).exists() }.forEach {
            val saxReader = SAXReader()
            try {
                val document = saxReader.read(File(it))
                val rootElement = document.rootElement
                if (rootElement.name == "checkstyle") {
                    rootElement.elements().map { i -> i as Element }.forEach { e ->
                        val name = e.attributeValue("name")
                        e.elements().forEach { i ->
                            val element = i as Element
                            val source = element.attributeValue("source")
                            val message = element.attributeValue("message")
                            val severity = element.attributeValue("severity")
                            val line = element.attributeValue("line").toInt()
                            val column = element.attributeValue("column").orEmpty().toIntOrNull() ?: 0
                            reports.add(CheckStyle(UUID.randomUUID().toString(), name, source, message, line, column, severity))
                        }
                    }
                }
            } catch (e: Exception) {
                log.warn("$it is not supported by checkstyle")
            }
        }
        return reports
    }

    private fun save(reports: ArrayList<CheckStyle>) {
        checkStyleRepo.deleteAll()
        checkStyleRepo.save(reports)
    }
}
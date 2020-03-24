package com.thoughtworks.archgard.scanner.domain.scanner.checkstyle

import com.thoughtworks.archgard.scanner.domain.ScanContext
import com.thoughtworks.archgard.scanner.domain.scanner.Scanner
import org.dom4j.Element
import org.dom4j.io.SAXReader
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

@Service
class CheckstyleScanner : Scanner {

    @Autowired
    private lateinit var checkStyleRepo: CheckStyleRepo

    private lateinit var buildTool: String

    private lateinit var reportFile: ArrayList<String>

    val reports = ArrayList<CheckStyle>()

    override fun scan(context: ScanContext) {
        getInfo()
        analysis()
        save()
    }

    private fun getInfo() {
        reportFile = ArrayList()
    }

    private fun analysis() {
        reportFile.forEach {
            val saxReader = SAXReader()
            val document = saxReader.read(File(it))
            val rootElement = document.rootElement
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
    }

    private fun save() {
        checkStyleRepo.save(reports)
    }
}
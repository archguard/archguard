package org.archguard.scanner.analyser.xml

import org.archguard.scanner.analyser.xml.mybatis.MyBatisHandler
import org.archguard.scanner.analyser.xml.mybatis.MybatisEntry
import org.slf4j.LoggerFactory
import org.xml.sax.SAXException
import java.io.File
import javax.xml.parsers.ParserConfigurationException
import javax.xml.parsers.SAXParserFactory

class XmlParser(
    val contentHandler: BasedXmlHandler,
    val filePath: String,
    val handlerName: String
) {
    fun processMyBatis(): MybatisEntry? {
        when(this.handlerName) {
            "MyBatisHandler" -> {
                val mybatis = this.contentHandler as MyBatisHandler
                return mybatis.compute(filePath)
            }
        }

        return null
    }

    companion object {
        fun parseMybatis(path: String): List<MybatisEntry> {
            return File(path)
                .walk()
                .filter { it.isFile && it.extension == "xml" }
                .mapNotNull {
                    fromFile(it.toString())?.processMyBatis()
                }.toList()
        }

        @Throws(ParserConfigurationException::class, SAXException::class)
        fun fromFile(filePath: String): XmlParser? {
            val inputSource = File(filePath)
            val parser = SAXParserFactory.newInstance().newSAXParser()

            // 1. first detect xml handler
            val dispatcher = HandlerDispatcher()
            parser.setProperty("http://xml.org/sax/properties/lexical-handler", dispatcher)

            try {
                parser.parse(inputSource, dispatcher)
            } catch(e: Exception) {
                LoggerFactory.getLogger(XmlParser.javaClass).info(e.toString())
                return null
            }

            val contentHandler = dispatcher.getContentHandler() ?: return null

            // 2. choice handler by types
            val xmlReader = parser.xmlReader
            xmlReader.contentHandler = contentHandler
            xmlReader.parse(filePath)

            return XmlParser(contentHandler, filePath, dispatcher.handlerName())
        }
    }
}

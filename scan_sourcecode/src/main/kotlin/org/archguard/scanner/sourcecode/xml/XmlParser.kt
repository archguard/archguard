package org.archguard.scanner.sourcecode.xml

import org.archguard.scanner.sourcecode.xml.mybatis.MyBatisHandler
import org.archguard.scanner.sourcecode.xml.mybatis.MybatisEntry
import org.xml.sax.SAXException
import org.xml.sax.XMLReader
import java.io.File
import java.nio.file.Path
import javax.xml.parsers.ParserConfigurationException
import javax.xml.parsers.SAXParserFactory

class XmlParser(
    val xmlReader: XMLReader,
    val contentHandler: BasedXmlHandler,
    val filePath: String,
    val handlerName: String
) {

    fun parseMyBatis(): MybatisEntry? {
        when(this.handlerName) {
            "MyBatisHandler" -> {
                val mybatis = this.contentHandler as MyBatisHandler
                return mybatis.compute(filePath)
            }
        }

        return null
    }

    companion object {
        @Throws(ParserConfigurationException::class, SAXException::class)
        fun fromPath(file: Path): XmlParser? {
            val filePath = file.toString()
            val inputSource = File(filePath)
            val parser = SAXParserFactory.newInstance().newSAXParser()

            // 1. first detect xml handler
            val dispatcher = HandlerDispatcher()
            parser.setProperty("http://xml.org/sax/properties/lexical-handler", dispatcher)

            parser.parse(inputSource, dispatcher)

            val contentHandler = dispatcher.getContentHandler() ?: return null

            // 2. choice handler by types
            val xmlReader = parser.xmlReader
            xmlReader.contentHandler = contentHandler
            xmlReader.parse(filePath)

            return XmlParser(xmlReader, contentHandler, filePath, dispatcher.handlerName())
        }
    }
}
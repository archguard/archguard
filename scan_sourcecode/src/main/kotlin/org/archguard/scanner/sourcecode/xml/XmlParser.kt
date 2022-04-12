package org.archguard.scanner.sourcecode.xml

import org.xml.sax.SAXException
import java.io.File
import java.nio.file.Path
import javax.xml.parsers.ParserConfigurationException
import javax.xml.parsers.SAXParserFactory

class XmlParser {
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

            contentHandler.compute(filePath)

            return XmlParser()
        }
    }
}
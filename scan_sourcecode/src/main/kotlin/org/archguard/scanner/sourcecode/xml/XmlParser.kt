package org.archguard.scanner.sourcecode.xml

import org.xml.sax.SAXException
import org.xml.sax.XMLReader
import java.nio.file.Path
import javax.xml.parsers.ParserConfigurationException
import javax.xml.parsers.SAXParserFactory

class XmlParser(val handler: HandlerDispatch, val xmlReader: XMLReader) {

    fun byFile(file: Path) {
        xmlReader.parse(file.toString())
    }

    companion object {
        @Throws(ParserConfigurationException::class, SAXException::class)
        fun fromPath(): XmlParser {
            val parser = SAXParserFactory.newInstance().newSAXParser()
            val xmlReader = parser.xmlReader

            // 1. first detect xml handler
            val handler = HandlerDispatch()

            // 2. choice handler by types
            xmlReader.dtdHandler = handler
            xmlReader.contentHandler = handler

            return XmlParser(handler, xmlReader)
        }
    }
}
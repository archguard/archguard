package org.archguard.scanner.sourcecode.xml

import org.xml.sax.helpers.DefaultHandler

open class BasedXmlHandler: DefaultHandler() {
    open fun name(): String {
        return ""
    }

    open fun detect(name: String?, publicId: String?, systemId: String?): Boolean {
        return false
    }

    open fun compute(filePath: String): XmlConfig {
        return EmptyXmlConfig()
    }
}
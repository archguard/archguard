package org.archguard.scanner.analyser.xml

import org.xml.sax.helpers.DefaultHandler

open class BasedXmlHandler: DefaultHandler() {
    open fun name(): String {
        return ""
    }

    open fun detect(name: String?, publicId: String?, systemId: String?): Boolean {
        return false
    }
}

package org.archguard.scanner.sourcecode.xml

import org.xml.sax.Attributes
import org.xml.sax.InputSource
import org.xml.sax.Locator
import org.xml.sax.ext.DefaultHandler2

// todo: use like Koin: https://insert-koin.io/docs/quickstart/kotlin
class HandlerDispatch: DefaultHandler2() {
    override fun unparsedEntityDecl(name: String?, publicId: String?, systemId: String?, notationName: String?) {
        super.unparsedEntityDecl(name, publicId, systemId, notationName)
    }

    override fun resolveEntity(publicId: String?, systemId: String?): InputSource {
        return super.resolveEntity(publicId, systemId)
    }

    override fun resolveEntity(name: String?, publicId: String?, baseURI: String?, systemId: String?): InputSource {
        return super.resolveEntity(name, publicId, baseURI, systemId)
    }

    override fun startEntity(name: String?) {
        super.startEntity(name)
    }

    override fun startCDATA() {
        super.startCDATA()
    }

    override fun elementDecl(name: String?, model: String?) {
        super.elementDecl(name, model)
    }

    override fun attributeDecl(eName: String?, aName: String?, type: String?, mode: String?, value: String?) {
        super.attributeDecl(eName, aName, type, mode, value)
    }

    override fun internalEntityDecl(name: String?, value: String?) {
        super.internalEntityDecl(name, value)
    }

    override fun externalEntityDecl(name: String?, publicId: String?, systemId: String?) {
        super.externalEntityDecl(name, publicId, systemId)
    }

    override fun getExternalSubset(name: String?, baseURI: String?): InputSource {
        return super.getExternalSubset(name, baseURI)
    }

    override fun startDTD(name: String?, publicId: String?, systemId: String?) {
        super.startDTD(name, publicId, systemId)
    }
    override fun setDocumentLocator(locator: Locator?) {
        super.setDocumentLocator(locator)
    }

    override fun notationDecl(name: String?, publicId: String?, systemId: String?) {
        super.notationDecl(name, publicId, systemId)
    }

    override fun startPrefixMapping(prefix: String?, uri: String?) {
        super.startPrefixMapping(prefix, uri)
    }

    override fun startElement(uri: String, localName: String, qName: String, attributes: Attributes?) {
        println(localName)
    }

}
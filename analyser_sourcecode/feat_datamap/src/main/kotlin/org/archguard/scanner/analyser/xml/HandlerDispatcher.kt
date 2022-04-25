package org.archguard.scanner.analyser.xml

import org.archguard.scanner.analyser.xml.mybatis.MyBatisHandler
import org.xml.sax.ext.DefaultHandler2

// todo: use like Koin: https://insert-koin.io/docs/quickstart/kotlin
class HandlerDispatcher: DefaultHandler2() {
    private var handlerName: String = ""
    private val mybatisHandler: MyBatisHandler = MyBatisHandler()

    override fun startDTD(name: String?, publicId: String?, systemId: String?) {
        if(mybatisHandler.detect(name, publicId, systemId)) {
            this.handlerName = mybatisHandler.name()
        }
    }

    fun getContentHandler(): BasedXmlHandler? {
        when(this.handlerName) {
            "MyBatisHandler" -> {
                return mybatisHandler
            }
        }

        return null
    }

    fun handlerName(): String {
        return this.handlerName
    }
}

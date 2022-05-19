package org.archguard.dsl.analyser

import org.archguard.dsl.base.Element

class Inbound : Element {
    fun key(key: String, value: Any): Any {
        return ""
    }
}

class WebApiDecl() : Element {
    fun inbound(inbound: Inbound.() -> Unit) {

    }
}

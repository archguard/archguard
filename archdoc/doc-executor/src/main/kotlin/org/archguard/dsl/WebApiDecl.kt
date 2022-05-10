package org.archguard.dsl

class Inbound : Element {
    fun key(key: String, value: Any): Any {
        return ""
    }
}

class WebApiDecl(name: String): Element {
    fun inbound(inbound: Inbound.() -> Unit) {

    }
}

fun api(name: String, init: WebApiDecl.() -> Unit): WebApiDecl {
    val webapiDecl = WebApiDecl(name)
    webapiDecl.init()
    return webapiDecl
}
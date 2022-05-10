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
    val webApiDecl = WebApiDecl(name)
    webApiDecl.init()

    archdoc.webApiDecl = webApiDecl

    return webApiDecl
}
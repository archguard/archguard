package org.archguard.dsl

class Inbound : Element {
    fun key(key: String, value: Any): Any {
        return ""
    }
}

class WebApiDecl() : Element {
    fun inbound(inbound: Inbound.() -> Unit) {

    }
}

fun api(name: String, init: WebApiDecl.() -> Unit): WebApiDecl {
    val webApiDecl = WebApiDecl()
    webApiDecl.init()

    archdoc.webapi = webApiDecl

    return webApiDecl
}
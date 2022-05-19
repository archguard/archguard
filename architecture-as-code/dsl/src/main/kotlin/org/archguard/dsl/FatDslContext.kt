package org.archguard.dsl

import org.archguard.dsl.action.ReactiveActionDecl
import org.archguard.dsl.analyser.WebApiDecl
import org.archguard.dsl.arch.LayeredDecl
import org.archguard.dsl.crud.ReposDecl

class FatDslContext(
    var layered: LayeredDecl = LayeredDecl(),
    var repos: ReposDecl = ReposDecl(),
    var webapi: WebApiDecl = WebApiDecl()
)

// todo: find a better way
val context = FatDslContext()

fun layered(init: LayeredDecl.() -> Unit): LayeredDecl {
    val layeredDecl = LayeredDecl()
    layeredDecl.init()
    context.layered = layeredDecl
    return layeredDecl
}

fun graph(): ReactiveActionDecl {
    return ReactiveActionDecl()
}

fun repos(init: ReposDecl.() -> Unit): ReposDecl {
    val reposDecl = ReposDecl()
    reposDecl.init()
    context.repos = reposDecl
    return reposDecl
}

fun api(name: String, init: WebApiDecl.() -> Unit): WebApiDecl {
    val webApiDecl = WebApiDecl()
    webApiDecl.init()
    context.webapi = webApiDecl
    return webApiDecl
}

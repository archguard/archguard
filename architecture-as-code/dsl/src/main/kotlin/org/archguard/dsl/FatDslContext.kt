package org.archguard.dsl

import org.archguard.dsl.action.ReactiveActionDecl
import org.archguard.dsl.design.LayeredArchDsl
import org.archguard.dsl.evolution.ReposDecl
import org.archguard.dsl.evolution.ScanModelDecl
import org.archguard.dsl.evolution.WebApiDecl
import org.archguard.dsl.insight.InsightDecl

class FatDslContext(
    var layered: LayeredArchDsl = LayeredArchDsl(),
    var repos: ReposDecl = ReposDecl(),
    var webapi: WebApiDecl = WebApiDecl(),
    var scan: ScanModelDecl? = null,
    var insight: InsightDecl? = null
) {

}

// todo: find a better way
val context = FatDslContext()

fun layered(init: LayeredArchDsl.() -> Unit): LayeredArchDsl {
    val layeredDecl = LayeredArchDsl()
    layeredDecl.init()
    context.layered = layeredDecl
    return layeredDecl
}

fun diagram(): ReactiveActionDecl {
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

fun scan(name: String, init: ScanModelDecl.() -> Unit = {}): ScanModelDecl {
    val scanDecl = ScanModelDecl(name)
    scanDecl.init()
    context.scan = scanDecl
    return scanDecl
}

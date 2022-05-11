package org.archguard.dsl


@DslMarker
annotation class DslMarker

interface Element

@DslMarker
abstract class Decl : Element {

}


class ArchDocContext(
    var layeredDecl: LayeredDecl? = null,
    var reposDecl: ReposDecl? = null,
    var webApiDecl: WebApiDecl? = null
)

// todo: find a better way
val archdoc = ArchDocContext()

package org.archguard.dsl


@DslMarker
annotation class DslMarker

interface Element

@DslMarker
abstract class Decl : Element

class ArchDocContext(
    var layered: LayeredDecl = LayeredDecl(),
    var repos: ReposDecl = ReposDecl(),
    var webapi: WebApiDecl = WebApiDecl()
)

// todo: find a better way
val archdoc = ArchDocContext()

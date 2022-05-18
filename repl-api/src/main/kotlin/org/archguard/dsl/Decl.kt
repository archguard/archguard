package org.archguard.dsl


@DslMarker
annotation class DslMarker

interface Element

@DslMarker
abstract class Decl : Element {

}


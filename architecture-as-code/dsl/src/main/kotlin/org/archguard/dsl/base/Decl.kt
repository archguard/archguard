package org.archguard.dsl.base


@DslMarker
annotation class DslMarker

interface Element {}

@DslMarker
abstract class Decl : Element

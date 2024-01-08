package org.archguard.model

data class Dependency<T>(val caller: T, val callee: T)

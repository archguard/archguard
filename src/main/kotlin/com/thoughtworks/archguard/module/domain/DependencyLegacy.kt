package com.thoughtworks.archguard.module.domain

@Deprecated(message = "we are going to replace with Dependency")
data class DependencyLegacy(val caller: String, val callee: String)

data class Dependency<T>(val caller: T, val callee: T)


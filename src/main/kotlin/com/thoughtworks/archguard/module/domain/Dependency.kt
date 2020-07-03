package com.thoughtworks.archguard.module.domain

data class Dependency(val caller: String, val callee: String)

data class NewDependency<T>(val caller: T, val callee: T)


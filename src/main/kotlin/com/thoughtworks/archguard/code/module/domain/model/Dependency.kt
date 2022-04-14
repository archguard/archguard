package com.thoughtworks.archguard.code.module.domain.model

data class Dependency<T>(val caller: T, val callee: T)

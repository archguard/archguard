package com.thoughtworks.archguard.module.domain.model

data class Dependency<T>(val caller: T, val callee: T)


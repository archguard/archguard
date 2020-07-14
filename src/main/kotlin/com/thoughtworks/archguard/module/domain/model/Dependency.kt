package com.thoughtworks.archguard.module.domain.model

open class Dependency<T>(val caller: T, val callee: T)


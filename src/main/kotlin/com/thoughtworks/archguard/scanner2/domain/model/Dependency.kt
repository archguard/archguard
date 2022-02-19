package com.thoughtworks.archguard.scanner2.domain.model

data class Dependency<T>(val caller: T, val callee: T)
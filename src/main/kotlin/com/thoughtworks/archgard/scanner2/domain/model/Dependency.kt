package com.thoughtworks.archgard.scanner2.domain.model

data class Dependency<T>(val caller: T, val callee: T)
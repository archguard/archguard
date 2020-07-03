package com.thoughtworks.archguard.module.domain

data class Dependency<T>(val caller: T, val callee: T)


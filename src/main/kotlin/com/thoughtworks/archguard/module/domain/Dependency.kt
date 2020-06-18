package com.thoughtworks.archguard.module.domain

data class Dependency(val caller: String, val callee: String)

package com.thoughtworks.archguard.module.domain.model

data class JMethodVO(val moduleName: String, val className: String,val name: String) {
    val fullName = "$moduleName.$className.$name"
}

package com.thoughtworks.archguard.module.domain.model

class PackageVO(val packageName: String, val module: String) {
    fun containClass(jClassVO: JClassVO): Boolean {
        return module == jClassVO.module && jClassVO.name.startsWith("$packageName.")
    }
}
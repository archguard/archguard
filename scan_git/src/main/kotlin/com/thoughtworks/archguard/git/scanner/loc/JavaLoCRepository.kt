package com.thoughtworks.archguard.git.scanner.loc

import com.thoughtworks.archguard.git.scanner.loc.model.JClassLoC

class JavaLoCRepository {
    private val store = JavaStoreImpl()
    fun save(loc: JClassLoC, systemId: String) {
        store.saveClassLoC(loc.pkg + "." + loc.clz, loc.loc, systemId, loc.module ?: ".")
    }

    fun toFile() {
        store.toFile()
    }

}
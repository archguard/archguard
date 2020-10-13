package com.thoughtworks.archguard.git.scanner.loc

import com.thoughtworks.archguard.git.scanner.loc.model.JClassLoC

class LoCRepository {
    private val store = LoCStoreImpl()
    fun save(loc: JClassLoC, systemId: String) {
        store.saveClassLoC(loc.pkg + "." + loc.clz, loc.loc, systemId, loc.module ?: ".", loc.methodLocs)
    }

    fun toFile() {
        store.toFile()
    }

}
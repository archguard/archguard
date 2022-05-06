package org.archguard.analyser.sca.base

import org.archguard.analyser.sca.model.DeclFileTree
import org.archguard.analyser.sca.model.PackageDependencies

abstract class Parser {
    open fun lookupSource(file: DeclFileTree): List<PackageDependencies> {
        return listOf()
    }

    open fun lookupChildDep(file: DeclFileTree): List<PackageDependencies> {
        return listOf()
    }
}

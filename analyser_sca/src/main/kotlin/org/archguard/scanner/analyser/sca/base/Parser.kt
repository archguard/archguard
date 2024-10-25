package org.archguard.scanner.analyser.sca.base

import org.archguard.model.DeclFileTree
import org.archguard.model.PackageDependencies

abstract class Parser {
    open fun lookupSource(file: DeclFileTree): List<PackageDependencies> {
        return listOf()
    }

    open fun lookupChildDep(file: DeclFileTree): List<PackageDependencies> {
        return listOf()
    }
}

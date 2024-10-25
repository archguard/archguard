package org.archguard.scanner.analyser.sca.base

import org.archguard.context.DeclFileTree
import org.archguard.context.PackageDependencies

abstract class Parser {
    open fun lookupSource(file: DeclFileTree): List<PackageDependencies> {
        return listOf()
    }

    open fun lookupChildDep(file: DeclFileTree): List<PackageDependencies> {
        return listOf()
    }
}

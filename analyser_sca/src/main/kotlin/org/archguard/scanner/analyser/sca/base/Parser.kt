package org.archguard.scanner.analyser.sca.base

import org.archguard.scanner.core.sca.DeclFileTree
import org.archguard.scanner.core.sca.PackageDependencies

abstract class Parser {
    open fun lookupSource(file: DeclFileTree): List<PackageDependencies> {
        return listOf()
    }

    open fun lookupChildDep(file: DeclFileTree): List<PackageDependencies> {
        return listOf()
    }
}

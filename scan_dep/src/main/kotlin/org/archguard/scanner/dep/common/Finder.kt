package org.archguard.scanner.dep.common

import org.archguard.scanner.dep.model.DeclFile
import org.archguard.scanner.dep.model.DepDecl

abstract class Finder {
    open fun lookupSource(file: DeclFile): List<DepDecl> {
        return listOf()
    }

    open fun lookupChildDep(file: DeclFile): List<DepDecl> {
        return listOf()
    }
}

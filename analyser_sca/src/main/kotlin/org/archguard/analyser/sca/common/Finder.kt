package org.archguard.analyser.sca.common

import org.archguard.analyser.sca.model.DeclFile
import org.archguard.analyser.sca.model.DepDecl

abstract class Finder {
    open fun lookupSource(file: DeclFile): List<DepDecl> {
        return listOf()
    }

    open fun lookupChildDep(file: DeclFile): List<DepDecl> {
        return listOf()
    }
}

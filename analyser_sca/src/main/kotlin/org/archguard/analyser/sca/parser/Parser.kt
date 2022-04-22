package org.archguard.analyser.sca.parser

import org.archguard.analyser.sca.model.DeclFileTree
import org.archguard.analyser.sca.model.DepDeclaration

abstract class Parser {
    open fun lookupSource(file: DeclFileTree): List<DepDeclaration> {
        return listOf()
    }

    open fun lookupChildDep(file: DeclFileTree): List<DepDeclaration> {
        return listOf()
    }
}

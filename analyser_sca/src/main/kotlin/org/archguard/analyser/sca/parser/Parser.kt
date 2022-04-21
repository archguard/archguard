package org.archguard.analyser.sca.parser

import org.archguard.analyser.sca.model.DeclFileTree
import org.archguard.analyser.sca.model.DepDecl

abstract class Parser {
    open fun lookupSource(file: DeclFileTree): List<DepDecl> {
        return listOf()
    }

    open fun lookupChildDep(file: DeclFileTree): List<DepDecl> {
        return listOf()
    }
}

package org.archguard.scanner.analyser.sca.gomod

import org.archguard.scanner.analyser.sca.base.Parser
import org.archguard.scanner.core.sca.DeclFileTree
import org.archguard.scanner.core.sca.PackageDependencies

class GoSumParser: Parser() {
    override fun lookupSource(file: DeclFileTree): List<PackageDependencies> {
        return listOf()
    }
}
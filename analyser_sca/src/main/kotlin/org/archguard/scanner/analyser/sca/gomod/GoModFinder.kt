package org.archguard.scanner.analyser.sca.gomod

import org.archguard.scanner.analyser.sca.base.Finder
import org.archguard.scanner.analyser.sca.base.Parser
import java.io.File

class GoModFinder: Finder() {
    override val parser: Parser = GoModParser()

    override fun isMatch(it: File): Boolean {
        return it.isFile && it.name == "go.mod"
    }
}

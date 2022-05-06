package org.archguard.analyser.sca.npm

import org.archguard.analyser.sca.base.Parser
import org.archguard.analyser.sca.base.Finder
import java.io.File

class NpmFinder: Finder() {
    override val parser: Parser = NpmParser()

    override fun isMatch(it: File): Boolean {
        return it.isFile && it.name == "package.json"
    }
}

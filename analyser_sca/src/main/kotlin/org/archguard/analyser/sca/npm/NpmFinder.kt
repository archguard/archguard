package org.archguard.analyser.sca.npm

import org.archguard.analyser.sca.parser.Parser
import org.archguard.analyser.sca.processor.ScaFinder
import java.io.File

class NpmFinder: ScaFinder() {
    override val parser: Parser = NpmParser()

    override fun isMatch(it: File): Boolean {
        return it.isFile && it.name == "package.json"
    }
}

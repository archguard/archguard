package org.archguard.analyser.sca.processor

import org.archguard.analyser.sca.parser.NpmParser
import org.archguard.analyser.sca.parser.Parser
import java.io.File

class JavaScriptFinder: ScaFinder() {
    override val parser: Parser = NpmParser()

    override fun isMatch(it: File): Boolean {
        return it.isFile && it.name == "package.json"
    }
}

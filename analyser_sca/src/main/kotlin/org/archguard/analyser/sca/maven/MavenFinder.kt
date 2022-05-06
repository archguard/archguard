package org.archguard.analyser.sca.maven

import org.archguard.analyser.sca.parser.Parser
import org.archguard.analyser.sca.processor.ScaFinder
import java.io.File

class MavenFinder: ScaFinder() {
    override val parser: Parser = MavenParser()

    override fun isMatch(it: File): Boolean {
        return it.isFile && it.name == "pom.xml"
    }
}

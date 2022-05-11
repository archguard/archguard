package org.archguard.scanner.analyser.sca.maven

import org.archguard.scanner.analyser.sca.base.Parser
import org.archguard.scanner.analyser.sca.base.Finder
import java.io.File

class MavenFinder: Finder() {
    override val parser: Parser = MavenParser()

    override fun isMatch(it: File): Boolean {
        return it.isFile && it.name == "pom.xml"
    }
}

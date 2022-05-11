package org.archguard.scanner.analyser.sca.gradle

import org.archguard.scanner.analyser.sca.base.Parser
import org.archguard.scanner.analyser.sca.base.Finder
import java.io.File

class GradleFinder: Finder() {
    override val parser: Parser = GradleParser()

    override fun isMatch(it: File): Boolean {
        return it.isFile && it.name == "build.gradle" || it.name == "build.gradle.kts"
    }
}

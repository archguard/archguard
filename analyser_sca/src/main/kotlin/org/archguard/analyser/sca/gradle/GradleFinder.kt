package org.archguard.analyser.sca.gradle

import org.archguard.analyser.sca.parser.Parser
import org.archguard.analyser.sca.processor.ScaFinder
import java.io.File

class GradleFinder: ScaFinder() {
    override val parser: Parser = GradleParser()

    override fun isMatch(it: File): Boolean {
        return it.isFile && it.name == "build.gradle" || it.name == "build.gradle.kts"
    }
}
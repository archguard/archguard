package org.archguard.doc.generator

import org.archguard.doc.generator.compiler.KotlinAnalysis
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.io.File

internal class CompilerTest {
    @Test
    @Disabled
    internal fun compiler_file() {
        val scriptFile = "src/main/kotlin/org/archguard/doc/generator/Runner.kt"
        val parser = KotlinAnalysis()
        val analyzeContext = parser.parse(File(scriptFile))

        println(analyzeContext)
    }
}
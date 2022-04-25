package org.archguard.doc.generator

import org.archguard.doc.generator.compiler.KotlinAnalysis
import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertEquals

internal class CompilerTest {
    @Test
    internal fun compiler_file() {
        val scriptFile = "src/"
        val parser = KotlinAnalysis()

        val analyzeContext = parser.parse(File(scriptFile))

        val clazz = analyzeContext.allClasses.filter {
            it.name.asString() == "KotlinAnalysis"
        }

        assertEquals(1, clazz.size)
    }
}
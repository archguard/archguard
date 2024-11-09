package org.archguard.scanner.analyser.database

import chapi.ast.goast.GoAnalyser
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.io.File

class GoSqlAnalyserTest {
    @Test
    fun should_identify_item() {
        val codeFile = javaClass.classLoader.getResource("go_database/subject.go").file
        val code = File(codeFile).readText()
        val ds = GoAnalyser().analysis(code, codeFile).DataStructures

        val analyser = GoSqlAnalyser()
        val result = ds.map {
            analyser.analysisByNode(it, "")
        }

        result.size shouldBe 1
    }
}

package com.thoughtworks.archgard.scanner.domain.toolscanners

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.thoughtworks.archgard.scanner.domain.bs.BadSmellService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.io.File

internal class CocaScannerTest {
    private val mapper = jacksonObjectMapper()

    @Test
    fun should_get_bad_smell_report() {
        val root = File(javaClass.classLoader.getResource("TestProject").toURI())
        val url = "http://ci.archguard.org/view/ThirdPartyTool/job/coca/lastSuccessfulBuild/artifact/coca_macos"
        val cocaScanner = CocaScanner(url, root)
        val badSmell = mapper.readValue<BadSmellService.CocaBadSmellModel>(cocaScanner.getBadSmellReport()).toBadSmell()
        assertEquals(badSmell.size, 2)
        assertEquals(badSmell.get(0).type, "long")
    }
}
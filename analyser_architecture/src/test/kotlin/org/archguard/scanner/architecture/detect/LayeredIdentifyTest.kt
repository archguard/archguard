package org.archguard.scanner.architecture.detect

import org.archguard.architecture.code.CodeStructureStyle
import org.archguard.scanner.analyser.layered.LayeredIdentify
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class LayeredIdentifyTest {
    @Test
    fun simple_ddd_match() {
        val packages: List<String> = listOf(
            "com.thoughtworks.archguard.change.application",
            "com.thoughtworks.archguard.change.controller",
            "com.thoughtworks.archguard.change.domain",
            "com.thoughtworks.archguard.change.infrastructure"
        )

        assertEquals(CodeStructureStyle.DDD, LayeredIdentify(packages).identify())
    }

    @Test
    fun simple_mvc_match() {
        val packages: List<String> = listOf(
            "com.thoughtworks.archguard.change.controller",
            "com.thoughtworks.archguard.change.service",
            "com.thoughtworks.archguard.change.repository"
        )

        assertEquals(CodeStructureStyle.MVC, LayeredIdentify(packages).identify())
    }
}
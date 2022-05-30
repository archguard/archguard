package org.archguard.scanner.core.sourcecode

import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertEquals

class ModuleIdentifyTest {
    @Test
    fun should_identify_root() {
        val rootProject = File("").absoluteFile.parentFile
        assert(ModuleIdentify.isRootModule(rootProject))
    }

    @Test
    fun should_collect_childrens() {
        val currentDir = File("").absoluteFile
        val rootProject = currentDir.parentFile

        val toList = rootProject
            .walk(FileWalkDirection.TOP_DOWN)
            .filter { ModuleIdentify.isSubModule(it) }.toList()

        assert(toList.contains(currentDir))
    }

    @Test
    fun path_to_name() {
        val currentDir = File("").absoluteFile
        val rootProject = currentDir.parentFile

        assertEquals(":scanner_core", ModuleIdentify.moduleName(currentDir, rootProject))
        assertEquals(":", ModuleIdentify.moduleName(rootProject, rootProject))

        val lang = File(File(currentDir.parentFile, "analyser_sourcecode"), "lang_golang")
        assertEquals(":analyser_sourcecode:lang_golang", ModuleIdentify.moduleName(lang, rootProject))
    }
}
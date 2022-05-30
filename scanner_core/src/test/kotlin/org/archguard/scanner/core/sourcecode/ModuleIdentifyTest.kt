package org.archguard.scanner.core.sourcecode

import org.junit.jupiter.api.Test
import java.io.File

class ModuleIdentifyTest {
    @Test
    fun should_identify_root() {
        val rootProject = File("").absoluteFile.parentFile
        val moduleIdentifier = ModuleIdentify()
        assert(moduleIdentifier.isRootModule(rootProject))
    }

    @Test
    fun should_collect_childrens() {
        val currentDir = File("").absoluteFile
        val rootProject = currentDir.parentFile
        val moduleIdentifier = ModuleIdentify()


        val toList = rootProject
            .walk(FileWalkDirection.TOP_DOWN)
            .filter {
                moduleIdentifier.isSubModule(it)
            }.toList()

        assert(toList.contains(currentDir))
    }
}
package org.archguard.scanner.bytecode.module

import org.junit.jupiter.api.Test
import java.nio.file.Paths
import kotlin.test.assertEquals

internal class ModuleUtilTest {
    @Test
    internal fun name() {
        val path = Paths.get(javaClass.classLoader.getResource("module/gradle").toURI())

        assertEquals("scan_bytecode", ModuleUtil.getModule(path).name)
        assertEquals(0, ModuleUtil.getModule(path).moduleDependencies!!.size)
    }
}
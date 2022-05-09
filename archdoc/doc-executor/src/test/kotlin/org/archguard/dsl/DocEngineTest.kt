package org.archguard.dsl

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class LayeredTest {
    @Test
    internal fun simple_layered() {
        val mvc = layered {
            prefixId("org.archguard")

            component("controller") dependentOn component("service")
            component("service") dependentOn component("repository")
        }

        assertEquals(3, mvc.components().size)
        assertEquals("controller", mvc.components()[0].name)
    }
}

package org.archguard.architecture.core

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class ComponentTest {
    @Test
    internal fun name() {
        val component = ExternalModuleDependencyComponent("root")

        val inbounds = listOf(GradleInbound(
            name = "root",
            artifact = "org.jdbi",
            group = "jdbi3-core",
            version = "3.8.2"
        ))

        component.ports += GradlePort(
            inbounds = inbounds
        )

        assertEquals(ArchComponentType.MODULE, component.type)
    }
}
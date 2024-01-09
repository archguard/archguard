package org.archguard.protocol.dubbo;

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class ReferenceConfigTest {

    @Test
    fun shouldReturnVersionsWhenHasSpecificVersions() {
        // given
        val version = "1.0, 2.0"
        val referenceConfig = ReferenceConfig(
            id = "1",
            beanId = "bean1",
            interfaceName = "Interface",
            version = version,
            group = null,
            subModule = SubModuleDubbo("1", "name", "path")
        )

        // when
        val versions = referenceConfig.getVersions()

        // then
        assertEquals(listOf("1.0", "2.0"), versions)
    }

    @Test
    fun shouldThrowExceptionWhenHasSpecificVersionsAndVersionIsNull() {
        // given
        val referenceConfig = ReferenceConfig(
            id = "1",
            beanId = "bean1",
            interfaceName = "Interface",
            version = null,
            group = null,
            subModule = SubModuleDubbo("1", "name", "path")
        )

        // when-then
        assertThrows<RuntimeException> {
            referenceConfig.getVersions()
        }
    }

    @Test
    fun shouldReturnGroupsWhenHasSpecificGroups() {
        // given
        val group = "group1, group2"
        val referenceConfig = ReferenceConfig(
            id = "1",
            beanId = "bean1",
            interfaceName = "Interface",
            version = null,
            group = group,
            subModule = SubModuleDubbo("1", "name", "path")
        )

        // when
        val groups = referenceConfig.getGroups()

        // then
        assertEquals(listOf("group1", "group2"), groups)
    }
}

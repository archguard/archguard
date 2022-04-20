package org.archguard.rule.impl.container

import org.archguard.rule.impl.container.model.ContainerResource
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class ContainerVisitorTest {
    @Test
    internal fun length_count() {
        val resource = ContainerResource(sourceUrl = "/api/fadlfkjaldfjaslfjalsfjalksdsj")
        val visitor = ContainerVisitor(arrayOf(resource))
        val ruleSetProvider = ContainerRuleSetProvider()

        val results = visitor.visitor(listOf(ruleSetProvider.get()), resource)
        assertEquals(1, results.size)
        assertEquals("UrlSplitNamingRule", results[0].name)
    }
}
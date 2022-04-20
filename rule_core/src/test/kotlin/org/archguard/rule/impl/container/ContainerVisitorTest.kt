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

    @Test
    internal fun not_end_with_create() {
        val resource = ContainerResource(sourceUrl = "/api/book/create")
        val visitor = ContainerVisitor(arrayOf(resource))
        val ruleSetProvider = ContainerRuleSetProvider()

        val results = visitor.visitor(listOf(ruleSetProvider.get()), resource)
        assertEquals(1, results.size)
        assertEquals("EndWithoutCrudRule", results[0].name)
    }

    @Test
    internal fun not_uppercase() {
        val resource = ContainerResource(sourceUrl = "/api/Book")
        val visitor = ContainerVisitor(arrayOf(resource))
        val ruleSetProvider = ContainerRuleSetProvider()

        val results = visitor.visitor(listOf(ruleSetProvider.get()), resource)
        assertEquals(1, results.size)
        assertEquals("NotUppercaseRule", results[0].name)
    }

    @Test
    internal fun start_without_crud() {
        val resource = ContainerResource(sourceUrl = "/api/getcfg")
        val visitor = ContainerVisitor(arrayOf(resource))
        val ruleSetProvider = ContainerRuleSetProvider()

        val results = visitor.visitor(listOf(ruleSetProvider.get()), resource)
        assertEquals(1, results.size)
        assertEquals("StartWithoutCrudRule", results[0].name)
    }
}
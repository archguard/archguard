package org.archguard.linter.rule.webapi

import org.archguard.scanner.core.sourcecode.ContainerResource
import org.archguard.scanner.core.sourcecode.ContainerService
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class WebApiRuleVisitorTest {
    @Test
    internal fun length_count() {
        val resource = ContainerResource(sourceUrl = "/api/fadlfkjaldfjaslfjalsfjalksdsj")
        val visitor = WebApiRuleVisitor(listOf(ContainerService(resources = listOf(resource))))
        val ruleSetProvider = WebApiRuleSetProvider()

        val results = visitor.visitor(listOf(ruleSetProvider.get()))
        assertEquals(1, results.size)
        assertEquals("SpliceNamingRule", results[0].name)
    }

    @Test
    internal fun not_end_with_create() {
        val resource = ContainerResource(sourceUrl = "/api/book/create")
        val visitor = WebApiRuleVisitor(listOf(ContainerService(resources = listOf(resource))))
        val ruleSetProvider = WebApiRuleSetProvider()

        val results = visitor.visitor(listOf(ruleSetProvider.get()))
        assertEquals(2, results.size)
        assertEquals("NoCrudEndRule", results[0].name)
    }

    @Test
    internal fun not_uppercase() {
        val resource = ContainerResource(sourceUrl = "/api/Book")
        val visitor = WebApiRuleVisitor(listOf(ContainerService(resources = listOf(resource))))
        val ruleSetProvider = WebApiRuleSetProvider()

        val results = visitor.visitor(listOf(ruleSetProvider.get()))
        assertEquals(1, results.size)
        assertEquals("NotUppercaseRule", results[0].name)
    }

    @Test
    internal fun start_without_crud() {
        val resource = ContainerResource(sourceUrl = "/api/getcfg")
        val visitor = WebApiRuleVisitor(listOf(ContainerService(resources = listOf(resource))))
        val ruleSetProvider = WebApiRuleSetProvider()

        val results = visitor.visitor(listOf(ruleSetProvider.get()))
        assertEquals(1, results.size)
        assertEquals("StartWithoutCrudRule", results[0].name)
    }

    @Test
    internal fun allow_parameter_in_url() {
        val resource = ContainerResource(sourceUrl = "api/book/{bookId}")
        val visitor = WebApiRuleVisitor(listOf(ContainerService(resources = listOf(resource))))
        val ruleSetProvider = WebApiRuleSetProvider()

        val results = visitor.visitor(listOf(ruleSetProvider.get()))
        assertEquals(0, results.size)
    }

    @Test
    internal fun not_method_in_node() {
        val resource = ContainerResource(sourceUrl = "api/book/delete/{bookId}")
        val visitor = WebApiRuleVisitor(listOf(ContainerService(resources = listOf(resource))))
        val ruleSetProvider = WebApiRuleSetProvider()

        val results = visitor.visitor(listOf(ruleSetProvider.get()))
        assertEquals(1, results.size)
        assertEquals("NoHttpMethodInUrl", results[0].name)
    }

    @Test
    internal fun multiple_parameters() {
        val resource = ContainerResource(sourceUrl = "/api/book/{bookType}/{bookId}/{bookChildType}/{childId}")
        val visitor = WebApiRuleVisitor(listOf(ContainerService(resources = listOf(resource))))
        val ruleSetProvider = WebApiRuleSetProvider()

        val results = visitor.visitor(listOf(ruleSetProvider.get()))
        assertEquals(1, results.size)
        assertEquals("MultipleParameters", results[0].name)
    }

    @Test
    internal fun min_feature() {
        val resource = ContainerResource(sourceUrl = "/api/book-with-author/")
        val visitor = WebApiRuleVisitor(listOf(ContainerService(resources = listOf(resource))))
        val ruleSetProvider = WebApiRuleSetProvider()

        val results = visitor.visitor(listOf(ruleSetProvider.get()))
        assertEquals(1, results.size)
        assertEquals("MinFeature", results[0].name)
    }
}

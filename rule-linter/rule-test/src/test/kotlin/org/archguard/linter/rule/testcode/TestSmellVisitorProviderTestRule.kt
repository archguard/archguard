package org.archguard.linter.rule.testcode

import chapi.domain.core.*
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class TestSmellVisitorProviderTestRule {
    @Test
    internal fun empty_test() {
        val provider = TestSmellRuleSetProvider()

        val ds = CodeDataStruct()
        ds.Functions += CodeFunction(
            Annotations = listOf(
                CodeAnnotation(Name = "Test"),
                CodeAnnotation(Name = "Ignore")
            )
        )

        val visitor = TestSmellRuleVisitor(listOf(ds))

        val results = visitor
            .visitor(listOf(provider.get()))

        assertEquals(3, results.size)
        assertEquals("EmptyTest", results[0].name)
        assertEquals("IgnoreTest", results[1].name)
    }

    @Test
    internal fun redundant_print() {
        val provider = TestSmellRuleSetProvider()

        val ds = CodeDataStruct()
        ds.Functions += CodeFunction(
            Annotations = listOf(CodeAnnotation(Name = "Test")),
            FunctionCalls = listOf(CodeCall(NodeName = "System.out", FunctionName = "println"))
        )

        val visitor = TestSmellRuleVisitor(listOf(ds))

        val results = visitor
            .visitor(listOf(provider.get()))

        assertEquals(2, results.size)
        assertEquals("RedundantPrintTest", results[0].name)
    }

    @Test
    internal fun sleepy() {
        val provider = TestSmellRuleSetProvider()

        val ds = CodeDataStruct()
        ds.Functions += CodeFunction(
            Annotations = listOf(CodeAnnotation(Name = "Test")),
            FunctionCalls = listOf(
                CodeCall(NodeName = "Thread", FunctionName = "sleep"),
                CodeCall(NodeName = "", FunctionName = "assert")
            )
        )

        val visitor = TestSmellRuleVisitor(listOf(ds))

        val results = visitor
            .visitor(listOf(provider.get()))

        assertEquals(1, results.size)
        assertEquals("SleepyTest", results[0].name)
    }

    @Test
    internal fun redundant_assertion() {
        val provider = TestSmellRuleSetProvider()

        val parameters: List<CodeProperty> = listOf(
            CodeProperty(TypeValue = "true", TypeType = "Boolean"),
            CodeProperty(TypeValue = "true", TypeType = "Boolean")
        )

        val ds = CodeDataStruct()
        ds.Functions += CodeFunction(
            Annotations = listOf(CodeAnnotation(Name = "Test")),
            FunctionCalls = listOf(CodeCall(NodeName = "", FunctionName = "assert", Parameters = parameters))
        )

        val visitor = TestSmellRuleVisitor(listOf(ds))

        val results = visitor
            .visitor(listOf(provider.get()))

        assertEquals(1, results.size)
        assertEquals("RedundantAssertionTest", results[0].name)
    }

    @Test
    internal fun unknown_test() {
        val provider = TestSmellRuleSetProvider()

        val parameters: List<CodeProperty> = listOf(
            CodeProperty(TypeValue = "true", TypeType = "Boolean"),
            CodeProperty(TypeValue = "true", TypeType = "Boolean")
        )

        val ds = CodeDataStruct()
        ds.Functions += CodeFunction(
            Annotations = listOf(CodeAnnotation(Name = "Test")),
            FunctionCalls = listOf(CodeCall(NodeName = "", FunctionName = "demo", Parameters = parameters))
        )

        val visitor = TestSmellRuleVisitor(listOf(ds))

        val results = visitor
            .visitor(listOf(provider.get()))

        assertEquals(1, results.size)
        assertEquals("UnknownTest", results[0].name)
    }

    @Test
    internal fun duplicate_asserts() {
        val provider = TestSmellRuleSetProvider()

        val parameters: List<CodeProperty> = listOf(CodeProperty(TypeValue = "true", TypeType = "Boolean"))

        val ds = CodeDataStruct()
        val assertCall = CodeCall(NodeName = "", FunctionName = "assert", Parameters = parameters)
        ds.Functions += CodeFunction(
            Annotations = listOf(CodeAnnotation(Name = "Test")),
            FunctionCalls = listOf(
                assertCall, assertCall, assertCall, assertCall, assertCall, assertCall
            )
        )

        val visitor = TestSmellRuleVisitor(listOf(ds))

        val results = visitor
            .visitor(listOf(provider.get()))

        assertEquals(1, results.size)
        assertEquals("DuplicateAssertTest", results[0].name)
    }

    @Test
    internal fun ignore_clz_ignore() {
        val provider = TestSmellRuleSetProvider()

        val ds = CodeDataStruct()
        ds.Annotations += CodeAnnotation(Name = "Ignore")

        ds.Functions += CodeFunction(
            Annotations = listOf(
                CodeAnnotation(Name = "Test"),
            )
        )

        val visitor = TestSmellRuleVisitor(listOf(ds))

        val results = visitor
            .visitor(listOf(provider.get()))

        assertEquals(3, results.size)
        assertEquals("EmptyTest", results[0].name)
        assertEquals("IgnoreTest", results[1].name)
    }

}

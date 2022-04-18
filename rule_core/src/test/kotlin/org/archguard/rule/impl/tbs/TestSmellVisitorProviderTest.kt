package org.archguard.rule.impl.tbs

import chapi.domain.core.CodeAnnotation
import chapi.domain.core.CodeCall
import chapi.domain.core.CodeDataStruct
import chapi.domain.core.CodeFunction
import chapi.domain.core.CodeProperty
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class TestSmellVisitorProviderTest {
    @Test
    internal fun empty_test() {
        val provider = TestSmellProvider()

        val ds = CodeDataStruct()
        ds.Functions += CodeFunction(
            Annotations = arrayOf(
                CodeAnnotation(Name = "Test"),
                CodeAnnotation(Name = "Ignore")
            )
        )

        val visitor = TestSmellVisitor(arrayOf(ds))

        val results = visitor
            .visitor(listOf(provider.get()), ds)

        assertEquals(3, results.size)
        assertEquals("EmptyTest", results[0].name)
        assertEquals("IgnoreTest", results[1].name)
    }

    @Test
    internal fun redundant_print() {
        val provider = TestSmellProvider()

        val ds = CodeDataStruct()
        ds.Functions += CodeFunction(
            Annotations = arrayOf(CodeAnnotation(Name = "Test")),
            FunctionCalls = arrayOf(CodeCall(NodeName = "System.out", FunctionName = "println"))
        )

        val visitor = TestSmellVisitor(arrayOf(ds))

        val results = visitor
            .visitor(listOf(provider.get()), ds)

        assertEquals(2, results.size)
        assertEquals("RedundantPrintTest", results[0].name)
    }

    @Test
    internal fun sleepy() {
        val provider = TestSmellProvider()

        val ds = CodeDataStruct()
        ds.Functions += CodeFunction(
            Annotations = arrayOf(CodeAnnotation(Name = "Test")),
            FunctionCalls = arrayOf(
                CodeCall(NodeName = "Thread", FunctionName = "sleep"),
                CodeCall(NodeName = "", FunctionName = "assert")
            )
        )

        val visitor = TestSmellVisitor(arrayOf(ds))

        val results = visitor
            .visitor(listOf(provider.get()), ds)

        assertEquals(1, results.size)
        assertEquals("SleepyTest", results[0].name)
    }

    @Test
    internal fun redundant_assertion() {
        val provider = TestSmellProvider()

        val parameters: Array<CodeProperty> = arrayOf(
            CodeProperty(TypeValue = "true", TypeType = "Boolean"),
            CodeProperty(TypeValue = "true", TypeType = "Boolean")
        )

        val ds = CodeDataStruct()
        ds.Functions += CodeFunction(
            Annotations = arrayOf(CodeAnnotation(Name = "Test")),
            FunctionCalls = arrayOf(CodeCall(NodeName = "", FunctionName = "assert", Parameters = parameters))
        )

        val visitor = TestSmellVisitor(arrayOf(ds))

        val results = visitor
            .visitor(listOf(provider.get()), ds)

        assertEquals(1, results.size)
        assertEquals("RedundantAssertionTest", results[0].name)
    }

    @Test
    internal fun unknown_test() {
        val provider = TestSmellProvider()

        val parameters: Array<CodeProperty> = arrayOf(
            CodeProperty(TypeValue = "true", TypeType = "Boolean"),
            CodeProperty(TypeValue = "true", TypeType = "Boolean")
        )

        val ds = CodeDataStruct()
        ds.Functions += CodeFunction(
            Annotations = arrayOf(CodeAnnotation(Name = "Test")),
            FunctionCalls = arrayOf(CodeCall(NodeName = "", FunctionName = "demo", Parameters = parameters))
        )

        val visitor = TestSmellVisitor(arrayOf(ds))

        val results = visitor
            .visitor(listOf(provider.get()), ds)

        assertEquals(1, results.size)
        assertEquals("UnknownTest", results[0].name)
    }

    @Test
    internal fun duplicate_asserts() {
        val provider = TestSmellProvider()

        val parameters: Array<CodeProperty> = arrayOf(CodeProperty(TypeValue = "true", TypeType = "Boolean"))

        val ds = CodeDataStruct()
        val assertCall = CodeCall(NodeName = "", FunctionName = "assert", Parameters = parameters)
        ds.Functions += CodeFunction(
            Annotations = arrayOf(CodeAnnotation(Name = "Test")),
            FunctionCalls = arrayOf(
                assertCall, assertCall, assertCall, assertCall, assertCall, assertCall
            )
        )

        val visitor = TestSmellVisitor(arrayOf(ds))

        val results = visitor
            .visitor(listOf(provider.get()), ds)

        assertEquals(1, results.size)
        assertEquals("DuplicateAssertTest", results[0].name)
    }
}

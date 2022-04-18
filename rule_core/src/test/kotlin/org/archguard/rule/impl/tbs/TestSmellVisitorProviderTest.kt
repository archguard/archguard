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

        assertEquals(2, results.size)
        assertEquals("EmptyTest", results[0].name)
        assertEquals("NoIgnoreTest", results[1].name)
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

        assertEquals(1, results.size)
        assertEquals("RedundantPrintTest", results[0].name)
    }

    @Test
    internal fun sleepy() {
        val provider = TestSmellProvider()

        val ds = CodeDataStruct()
        ds.Functions += CodeFunction(
            Annotations = arrayOf(CodeAnnotation(Name = "Test")),
            FunctionCalls = arrayOf(CodeCall(NodeName = "Thread", FunctionName = "sleep"))
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
        assertEquals("RedundantAssertionRuleTest", results[0].name)
    }
}

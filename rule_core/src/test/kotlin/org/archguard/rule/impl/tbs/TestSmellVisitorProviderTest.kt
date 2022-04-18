package org.archguard.rule.impl.tbs

import chapi.domain.core.CodeAnnotation
import chapi.domain.core.CodeDataStruct
import chapi.domain.core.CodeFunction
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class TestSmellVisitorProviderTest {
    @Test
    internal fun name() {
        val provider = TestSmellProvider()

        val ds = CodeDataStruct()
        ds.Functions += CodeFunction(Annotations = arrayOf(CodeAnnotation(Name = "Ignore")))

        val visitor = TestSmellVisitor(arrayOf(ds))

        val results = visitor
            .visitor(listOf(provider.get()), ds)

        assertEquals(2, results.size)
        assertEquals("EmptyTest", results[0].name)
        assertEquals("NoIgnoreTest", results[1].name)
    }
}

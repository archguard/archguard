package org.archguard.rule.impl.tbs

import chapi.domain.core.CodeAnnotation
import chapi.domain.core.CodeDataStruct
import chapi.domain.core.CodeFunction
import org.junit.jupiter.api.Test

internal class TestSmellVisitorProviderTest {
    @Test
    internal fun name() {
        val provider = TestSmellProvider()

        val ds = CodeDataStruct()
        ds.Functions += CodeFunction(Annotations = arrayOf(CodeAnnotation(Name = "Ignore")))

        val visitor = TestSmellVisitor(arrayOf(ds))

        visitor
            .visitor(listOf(provider.get()), ds)

    }
}

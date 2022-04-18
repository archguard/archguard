package org.archguard.rule.impl.tbs

import chapi.domain.core.CodeDataStruct
import org.junit.jupiter.api.Test

internal class TestSmellVisitorProviderTest {
    @Test
    internal fun name() {
        val provider = TestSmellProvider()

        val ds = CodeDataStruct()

        val visitor = TestSmellVisitor(arrayOf(ds))

        visitor
            .visitor(listOf(provider.get()), ds)

    }
}

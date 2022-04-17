package org.archguard.rule.impl.tbs

import chapi.domain.core.CodeDataStruct
import org.junit.jupiter.api.Test

internal class TestSmellVisitorProviderTest {
    @Test
    internal fun name() {
        val provider = TestSmellProvider()
        val visitor = TestSmellVisitor()

        val ds = CodeDataStruct()

        visitor
            .visitor(listOf(provider.get()), ds)

    }
}

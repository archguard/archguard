package org.archguard.rule.impl.tbs

import org.junit.jupiter.api.Test

internal class TestSmellProviderTest {
    @Test
    internal fun sample() {
        val provider = TestSmellProvider().get()
        provider.rules.forEach {
            println(it.name)
            println(it.key)
        }
    }
}
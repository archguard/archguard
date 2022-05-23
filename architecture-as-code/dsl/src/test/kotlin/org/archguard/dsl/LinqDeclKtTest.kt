package org.archguard.dsl

import org.archguard.dsl.evolution.clazz
import org.archguard.dsl.evolution.implementation
import org.junit.jupiter.api.Test

internal class LinqDeclKtTest {
    @Test
    internal fun name() {
        clazz(implementation("BaseParser")).map {}
    }
}
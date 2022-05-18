package org.archguard.dsl

import org.junit.jupiter.api.Test

internal class ClassQueryDeclKtTest {
    @Test
    internal fun name() {
        clazz(implementation("BaseParser")).map {}
    }
}
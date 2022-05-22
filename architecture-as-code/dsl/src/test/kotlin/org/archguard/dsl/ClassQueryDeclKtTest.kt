package org.archguard.dsl

import org.archguard.dsl.governance.clazz
import org.archguard.dsl.governance.implementation
import org.junit.jupiter.api.Test

internal class ClassQueryDeclKtTest {
    @Test
    internal fun name() {
        clazz(implementation("BaseParser")).map {}
    }
}
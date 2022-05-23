package org.archguard.dsl.builtin

import org.archguard.dsl.repos
import org.junit.jupiter.api.Test

internal class ModelKtTest {
    @Test
    internal fun type_test() {
        val decls = repos {
            repo(name = "Backend", language = "Kotlin", scmUrl = "https://github.com/archguard/archguard")
            repo(name = "Backend", language = "Kotlin", scmUrl = "https://github.com/archguard/archguard")
        }

        type(decls)
    }
}
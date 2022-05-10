package org.archguard.dsl

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class DslTest {
    @Test
    internal fun simple_layered() {
        val mvc = layered {
            prefixId("org.archguard")

            component("controller") dependentOn component("service")
            组件("service") 依赖于 组件("repository")
        }

        assertEquals(3, mvc.components().size)
        assertEquals("controller", mvc.components()[0].name)
    }

    @Test
    internal fun web_api() {
        api("Blog") {
            inbound {
                key("title", String::class)
                key("description", "Blog")
            }
        }
    }

    @Test
    internal fun archguard_repos() {
        val decls = repos {
            repo(name = "Backend", language = "Kotlin", scmUrl = "https://github.com/archguard/archguard")
            repo(name = "Backend", language = "Kotlin", scmUrl = "https://github.com/archguard/archguard")
        }

        assertEquals(2, decls.repos.size)
    }
}

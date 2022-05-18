package org.archguard.dsl

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class DslTest {
    private val mvc = layered {
        prefixId("org.archguard")

        component("controller") dependentOn component("service")
        组件("service") 依赖于 组件("repository")
    }

    @Test
    internal fun simple_layered() {
        assertEquals(3, mvc.components().size)
        assertEquals("controller", mvc.components()[0].name)
    }

    @Test
    internal fun layered_relations() {
        assertEquals(2, mvc.relations().size)
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

        assertEquals(2, archdoc.repos.repos.size)
        assertEquals(2, decls.repos.size)
    }

    @Test
    internal fun reactive_action() {
        val action = graph().show(LayeredDecl(), "sample")
        assertEquals("graph", action.actionType)
        assertEquals("org.archguard.dsl.LayeredDecl", action.className)
        assertEquals("sample", action.graphType)
    }

    @Test
    internal fun reactive_action_list() {
        val action = graph().show(mvc.relations(), "sample")
        assertEquals("graph", action.actionType)
        assertEquals("java.util.ArrayList", action.className)
        assertEquals("sample", action.graphType)
        assertEquals(
            "[{\"source\":\"controller\",\"target\":\"service\"}, {\"source\":\"service\",\"target\":\"repository\"}]",
            action.data
        )
    }

    @Test
    internal fun layered_decl() {
        val layer = layered {
            prefixId("org.archguard")
            component("controller") dependentOn component("application")
            组件("application") 依赖于 组件("domain")
            组件("controller") 依赖于 组件("domain")
            组件("repository") 依赖于 组件("domain")
        }

        val action = graph().show(layer.relations())
        assertEquals(
            "[{\"source\":\"controller\",\"target\":\"application\"}, {\"source\":\"controller\",\"target\":\"domain\"}, {\"source\":\"application\",\"target\":\"domain\"}, {\"source\":\"repository\",\"target\":\"domain\"}]",
            action.data
        )
    }
}

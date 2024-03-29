package org.archguard.dsl

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.archguard.dsl.base.model.ActionType
import org.archguard.dsl.base.model.GraphType
import org.archguard.dsl.design.LayeredArchDsl
import org.archguard.dsl.evolution.ScanModel
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

        assertEquals(2, context.repos.repos.size)
        assertEquals(2, decls.repos.size)
    }

    @Test
    internal fun reactive_action() {
        val action = diagram().show(LayeredArchDsl(), GraphType.UML)
        assertEquals(ActionType.GRAPH, action.actionType)
        assertEquals("org.archguard.dsl.design.LayeredArchDsl", action.className)
        assertEquals(GraphType.UML, action.graphType)
    }

    @Test
    internal fun reactive_action_list() {
        val action = diagram().show(mvc.relations(), GraphType.ARCHDOC)
        assertEquals(ActionType.GRAPH, action.actionType)
        assertEquals("java.util.ArrayList", action.className)
        assertEquals(GraphType.ARCHDOC, action.graphType)
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

        val action = diagram().show(layer.relations())
        assertEquals(
            "[{\"source\":\"controller\",\"target\":\"application\"}, {\"source\":\"controller\",\"target\":\"domain\"}, {\"source\":\"application\",\"target\":\"domain\"}, {\"source\":\"repository\",\"target\":\"domain\"}]",
            action.data
        )
    }

    @Test
    internal fun create_repo() {
        val decls = repos {
            repo(name = "Backend", language = "Kotlin", scmUrl = "https://github.com/archguard/archguard")
            repo(name = "Backend", language = "Kotlin", scmUrl = "https://github.com/archguard/archguard")
        }

        val action = decls.create()
        assertEquals(ActionType.CREATE_REPOS, action.actionType)
    }

    @Test
    internal fun test_scan() {
        val scan = scan("Backend") {
            languages("java")
            languages += "kotlin"
            spec("api")
            feature("sample")
            branch("master")
            type("")
        }

        val reactiveAction = scan.create()
        val model: ScanModel = Json.decodeFromString(reactiveAction.data)
        assertEquals("Backend", model.name)
        assertEquals("master", model.branch)
        assertEquals(listOf("api"), model.specs)
        assertEquals(listOf("java", "kotlin"), model.languages)
        assertEquals(listOf("sample"), model.features)

        val scan2: ScanModel = Json.decodeFromString(scan("Backend2").create().data)
        assertEquals("Backend2", scan2.name)
    }
}

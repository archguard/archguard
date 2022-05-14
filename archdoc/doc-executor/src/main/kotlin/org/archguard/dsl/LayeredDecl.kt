package org.archguard.dsl

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class ComponentDecl(val name: String) : Element {
    var dependents: List<ComponentDecl> = listOf()

    infix fun dependentOn(component: ComponentDecl) {
        this.dependents += component
    }

    infix fun `依赖于`(component: ComponentDecl) {
        this.dependents += component
    }
}


@Serializable
data class LayeredRelation(val source: String, val target: String) {
    override fun toString(): String {
        return Json.encodeToString(this)
    }
}

class LayeredDecl : Decl() {
    private var componentDecls: HashMap<String, ComponentDecl> = hashMapOf()

    private var prefix: String = ""

    fun prefixId(id: String) {
        this.prefix = id
    }

    fun `组件`(name: String): ComponentDecl {
        return this.component(name)
    }

    fun component(name: String): ComponentDecl {
        val componentDecl = ComponentDecl(name)
        this.componentDecls[name] = componentDecl
        return componentDecl
    }

    fun components(): List<ComponentDecl> {
        return componentDecls.map { it.value }
    }

    fun relations(): List<LayeredRelation> {
        return componentDecls.flatMap {
            it.value.dependents.map { comp ->
                LayeredRelation(it.key, comp.name)
            }.toList()
        }
    }
}

fun layered(init: LayeredDecl.() -> Unit): LayeredDecl {
    val layeredDecl = LayeredDecl()
    layeredDecl.init()

    archdoc.layeredDecl = layeredDecl

    return layeredDecl
}
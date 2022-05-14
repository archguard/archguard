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
        this.dependentOn(component)
    }
}


@Serializable
data class LayeredRelation(val source: String, val target: String) {
    override fun toString(): String {
        return Json.encodeToString(this)
    }
}

class LayeredDecl : Decl() {
    private var componentDeclMap: HashMap<String, ComponentDecl> = hashMapOf()

    private var prefix: String = ""

    fun prefixId(id: String) {
        this.prefix = id
    }

    fun `组件`(name: String): ComponentDecl = this.component(name)

    fun component(name: String): ComponentDecl {
        val componentDecl = if(this.componentDeclMap[name] != null) {
            this.componentDeclMap[name]!!
        } else {
            ComponentDecl(name)
        }


        this.componentDeclMap[name] = componentDecl
        return componentDecl
    }

    fun components(): List<ComponentDecl> {
        return componentDeclMap.map { it.value }
    }

    fun relations(): List<LayeredRelation> {
        return componentDeclMap.flatMap {
            it.value.dependents.map { comp ->
                LayeredRelation(it.key, comp.name)
            }.toList()
        }
    }
}

fun layered(init: LayeredDecl.() -> Unit): LayeredDecl {
    val layeredDecl = LayeredDecl()
    layeredDecl.init()

    archdoc.layered = layeredDecl

    return layeredDecl
}
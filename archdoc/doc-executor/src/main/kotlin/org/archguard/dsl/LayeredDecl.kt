package org.archguard.dsl

interface Element

class ComponentDecl(val name: String) : Element {
    private var dependents: List<ComponentDecl> = listOf()

    infix fun dependentOn(component: ComponentDecl) {
        this.dependents += component
    }

    infix fun `依赖于`(component: ComponentDecl) {
        this.dependents += component
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
}

fun layered(init: LayeredDecl.() -> Unit): LayeredDecl {
    val layeredDecl = LayeredDecl()
    layeredDecl.init()
    return layeredDecl
}
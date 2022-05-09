package org.archguard.dsl


@DslMarker
annotation class LayeredDslMarker

interface Element

class DependentOn : Element {
    infix fun component(name: String): ComponentDecl {
        return ComponentDecl(name)
    }

}

class ComponentDecl(val name: String) : Element {
    private var dependents: List<ComponentDecl> = listOf()

    infix fun dependentOn(component: ComponentDecl) {
        this.dependents += component
    }
}

@LayeredDslMarker
abstract class Decl : Element {

}

class Layered : Decl() {
    private var componentDecls: HashMap<String, ComponentDecl> = hashMapOf()

    private var prefix: String = ""

    fun prefixId(id: String) {
        this.prefix = id
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

fun layered(init: Layered.() -> Unit): Layered {
    val layered = Layered()
    layered.init()
    return layered
}
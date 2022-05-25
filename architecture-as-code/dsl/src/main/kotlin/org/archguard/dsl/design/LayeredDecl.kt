package org.archguard.dsl.design

import org.archguard.dsl.base.Decl
import org.archguard.dsl.base.Element
import org.archguard.dsl.base.model.LayeredRelation

class ComponentDecl(val name: String) : Element {
    var dependents: List<ComponentDecl> = listOf()

    infix fun dependentOn(component: ComponentDecl) {
        this.dependents += component
    }

    infix fun `依赖于`(component: ComponentDecl) {
        this.dependentOn(component)
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

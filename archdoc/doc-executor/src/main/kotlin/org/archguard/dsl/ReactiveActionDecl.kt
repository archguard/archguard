package org.archguard.dsl

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class ReactiveActionDecl : Element {

}

@Serializable
data class Action(
    val actionType: String,
    val className: String,
    val graphType: String
) {
    override fun toString(): String {
        return Json.encodeToString(this)
    }
}

class ReactiveAction : Element {
    fun show(decl: Element, graphType: String = "archdoc"): Action {
        return Action(actionType = "graph", className = decl.javaClass.name, graphType = graphType)
    }
}

fun graph(): ReactiveAction {
    return ReactiveAction()
}
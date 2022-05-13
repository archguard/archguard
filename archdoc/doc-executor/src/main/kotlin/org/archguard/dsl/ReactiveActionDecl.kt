package org.archguard.dsl

class ReactiveActionDecl : Element {

}


class Action(
    // graph or else?
    val actionType: String,
    val className: String,
    val graphType: String
)

class ReactiveAction : Element {
    fun show(decl: Element, graphType: String = "archdoc"): Action {
        return Action(actionType = "graph", className = decl.javaClass.name, graphType = graphType)
    }
}

fun graph(): ReactiveAction {
    return ReactiveAction()
}
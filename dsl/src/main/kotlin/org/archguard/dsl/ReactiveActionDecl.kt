package org.archguard.dsl

import org.archguard.dsl.model.ReactiveAction

class ReactiveActionDecl : Element {
    fun show(decl: Any, graphType: String = "archdoc"): ReactiveAction {
        return ReactiveAction(actionType = "graph", className = decl.javaClass.name, graphType = graphType, decl.toString())
    }
}

fun graph(): ReactiveActionDecl {
    return ReactiveActionDecl()
}
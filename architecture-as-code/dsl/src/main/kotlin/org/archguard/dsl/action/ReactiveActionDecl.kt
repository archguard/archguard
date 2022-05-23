package org.archguard.dsl.action

import org.archguard.dsl.base.Element
import org.archguard.dsl.base.model.ActionType
import org.archguard.dsl.base.model.GraphType
import org.archguard.dsl.base.model.ReactiveAction

class ReactiveActionDecl : Element {
    fun show(decl: Any, graphType: GraphType = GraphType.ARCHDOC): ReactiveAction {
        return ReactiveAction(actionType = ActionType.GRAPH, className = decl.javaClass.name, graphType = graphType, decl.toString())
    }
}

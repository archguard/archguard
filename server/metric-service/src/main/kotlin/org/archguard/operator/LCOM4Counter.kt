package org.archguard.operator

import org.archguard.graph.GraphStore
import org.archguard.model.code.JClass
import org.archguard.model.vos.JMethodVO

object LCOM4Graph {
    fun buildGraph(jClass: JClass): GraphStore {
        val graphStore = GraphStore()
        val methods = jClass.methods
        methods.forEach { method ->
            method.fields.forEach { graphStore.addEdge(JMethodVO.fromJMethod(method), it) }

            val methodsCallBySelfOtherMethod =
                method.callees.filter { jMethod -> methods.map { it.id }.contains(jMethod.id) }

            methodsCallBySelfOtherMethod.forEach {
                graphStore.addEdge(
                    JMethodVO.fromJMethod(method),
                    JMethodVO.fromJMethod(it)
                )
            }
        }

        return graphStore
    }

}

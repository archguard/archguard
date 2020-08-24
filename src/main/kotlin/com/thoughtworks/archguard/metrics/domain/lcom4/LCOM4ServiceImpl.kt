package com.thoughtworks.archguard.metrics.domain.lcom4

import com.thoughtworks.archguard.clazz.domain.JClass
import com.thoughtworks.archguard.module.domain.graph.GraphStore
import org.springframework.stereotype.Service

@Service
class LCOM4ServiceImpl : LCOM4Service {
    override fun getLCOM4Graph(jClass: JClass): GraphStore {
        val graphStore: GraphStore = GraphStore()
        val methods = jClass.methods
        methods.forEach { method ->
            method.fields.forEach { graphStore.addEdge(method.toVO(), it) }
            val methodsCallBySelfOtherMethod = method.callees.filter { jMethod -> methods.map { it.id }.contains(jMethod.id) }
            methodsCallBySelfOtherMethod.forEach { graphStore.addEdge(method.toVO(), it.toVO()) }
        }
        return graphStore
    }
}
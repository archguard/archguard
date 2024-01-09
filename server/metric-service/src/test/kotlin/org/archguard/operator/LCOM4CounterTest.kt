package org.archguard.operator;

import org.archguard.graph.Edge
import org.archguard.model.code.JClass
import org.archguard.model.code.JField
import org.archguard.model.code.JMethod
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class LCOM4CounterTest {

    @Test
    internal fun should_get_lcom4_graph() {
        val jClass = JClass("id1", "clazz1", "module1")
        val jField1 = JField("f1", "f1", "String")
        val jField2 = JField("f2", "f2", "Int")
        jClass.fields = listOf(jField1, jField2)
        val jMethod1 = JMethod("m1", "m1", "clazz1", "module1", "void", emptyList())
        jMethod1.fields = listOf(jField1)
        val jMethod2 = JMethod("m2", "m2", "clazz1", "module1", "String", emptyList())
        jMethod2.fields = listOf(jField2)
        val jMethod3 = JMethod("m3", "m3", "clazz1", "module1", "Boolean", emptyList())
        val jMethod4 = JMethod("m4", "m4", "clazz2", "module2", "Boolean", emptyList())
        jMethod3.callees = listOf(jMethod2, jMethod4)
        jClass.methods = listOf(jMethod1, jMethod2, jMethod3)
        val lcom4Graph = LCOM4Graph.buildGraph(jClass)

        Assertions.assertThat(lcom4Graph.toDirectedGraph().nodes.size).isEqualTo(5)
        Assertions.assertThat(lcom4Graph.toDirectedGraph().edges.size).isEqualTo(3)
        Assertions.assertThat(lcom4Graph.toDirectedGraph().edges)
            .contains(Edge("m1", "f1", 1), Edge("m2", "f2", 1), Edge("m3", "m2", 1))
    }

    @Test
    internal fun should_return_true_when_class_is_data_class() {
        val jClass = JClass("c1", "class1", "module1")
        val jField1 = JField("j1", "hello", "boolean")
        val jField2 = JField("j2", "goodField", "java.lang.String")
        jClass.fields = listOf(jField1, jField2)
        val jMethod1 = JMethod("m1", "isHello", "class1", "module1", "boolean", emptyList())
        val jMethod2 = JMethod("m2", "setHello", "class1", "module1", "void", listOf("boolean"))
        val jMethod3 = JMethod("m3", "getGoodField", "class1", "module1", "java.lang.String", emptyList())
        val jMethod4 = JMethod("m4", "setGoodField", "class1", "module1", "void", listOf("java.lang.String"))
        jClass.methods = listOf(jMethod1, jMethod2, jMethod3, jMethod4)

        Assertions.assertThat(checkIsDataClass(jClass)).isTrue()
    }
}
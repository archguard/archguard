package com.thoughtworks.archguard.code.module.domain.graph

import com.thoughtworks.archguard.v2.frontier.clazz.domain.JField
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class DfsServiceTest {
    @Test
    internal fun `should get connectivity count of graph`() {
        val jField1 = JField("f1", "f1", "String")
        val jField2 = JField("f2", "f2", "Int")
        val jField3 = JField("f3", "f3", "Double")
        val jField4 = JField("f4", "f4", "Double")
        val jField5 = JField("f5", "f5", "Double")
        val jField6 = JField("f6", "f6", "Double")

        val graph = GraphStore()
        val dfsService0 = DfsService(graph.toUndirectedGraph())
        assertThat(dfsService0.getConnectivityCount()).isEqualTo(0)

        graph.addEdge(jField1, jField2)
        graph.addEdge(jField3, jField4)
        graph.addEdge(jField5, jField6)

        val dfsService1 = DfsService(graph.toUndirectedGraph())
        assertThat(dfsService1.getConnectivityCount()).isEqualTo(3)

        graph.addEdge(jField1, jField3)
        val dfsService2 = DfsService(graph.toUndirectedGraph())
        assertThat(dfsService2.getConnectivityCount()).isEqualTo(2)
    }
}

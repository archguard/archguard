package org.archguard.graph.code;

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class PackageStoreTest {

    @Test
    fun shouldAddNodeAndGetNodeId() {
        // given
        val packageStore = PackageStore()

        // when
        val nodeId = packageStore.addNode("org.archguard.graph")

        // then
        assertTrue(packageStore.nodeContains("org.archguard.graph"))
        assertEquals(1, nodeId)
    }

    @Test
    fun shouldNotAddNodeAndGetExistingNodeId() {
        // given
        val packageStore = PackageStore()
        packageStore.addNode("org.archguard.graph")

        // when
        val nodeId = packageStore.addNode("org.archguard.graph")

        // then
        assertTrue(packageStore.nodeContains("org.archguard.graph"))
        assertEquals(1, nodeId)
    }

    @Test
    fun shouldAddEdge() {
        // given
        val packageStore = PackageStore()

        // when
        packageStore.addEdge("org.archguard.graph", "org.archguard.code", 5)

        // then
        val packageGraph = packageStore.getPackageGraph()
        assertEquals(1, packageGraph.edges.size)
        assertEquals(4, packageGraph.edges[0].a)
        assertEquals(3, packageGraph.edges[0].b)
        assertEquals(5, packageGraph.edges[0].num)
    }

    @Test
    fun shouldGetPackageGraph() {
        // given
        val packageStore = PackageStore()
        packageStore.addNode("org.archguard.graph")
        packageStore.addNode("org.archguard.code")
        packageStore.addEdge("org.archguard.graph", "org.archguard.code", 5)

        // when
        val packageGraph = packageStore.getPackageGraph()

        // then
        assertEquals(4, packageGraph.nodes.size)
        assertEquals(1, packageGraph.nodes[0].id)
        assertEquals("org", packageGraph.nodes[0].name)
        assertEquals(0, packageGraph.nodes[0].parent)
        assertEquals(2, packageGraph.nodes[1].id)
        assertEquals("archguard", packageGraph.nodes[1].name)
        assertEquals(1, packageGraph.nodes[1].parent)
        assertEquals(1, packageGraph.edges.size)
        assertEquals(4, packageGraph.edges[0].a)
        assertEquals(3, packageGraph.edges[0].b)
        assertEquals(5, packageGraph.edges[0].num)
    }
}

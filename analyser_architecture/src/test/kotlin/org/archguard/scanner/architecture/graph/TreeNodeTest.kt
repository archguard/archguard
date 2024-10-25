package org.archguard.scanner.architecture.graph;

import org.archguard.architecture.graph.TreeNode
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class TreeNodeTest {

    @Test
    fun should_buildPackageTree() {
        // given
        val packageNames = listOf("org.archguard", "org.archguard.architecture", "org.archguard.backend")

        // when
        val root = TreeNode.create(packageNames)

        // then
        assertEquals("root", root.name)
        assertEquals(1, root.children.size)

        val orgNode = root.children[0]
        assertEquals("org", orgNode.name)
        assertEquals(1, orgNode.children.size)

        val archguardNode = orgNode.children[0]
        assertEquals("archguard", archguardNode.name)
        assertEquals(2, archguardNode.children.size)

        val architectureNode = archguardNode.children[0]
        assertEquals("architecture", architectureNode.name)
        assertEquals(0, architectureNode.children.size)

        val backendNode = archguardNode.children[1]
        assertEquals("backend", backendNode.name)
        assertEquals(0, backendNode.children.size)
    }

    @Test
    fun should_findOrCreateChild_when_childExists() {
        // given
        val parent = TreeNode("parent")
        val child1 = TreeNode("child1")
        val child2 = TreeNode("child2")
        parent.children.add(child1)
        parent.children.add(child2)

        // when
        val result = TreeNode.findOrCreateChild(parent, "child1")

        // then
        assertEquals(child1, result)
    }

    @Test
    fun should_findOrCreateChild_when_childDoesNotExist() {
        // given
        val parent = TreeNode("parent")
        val child1 = TreeNode("child1")
        val child2 = TreeNode("child2")
        parent.children.add(child1)
        parent.children.add(child2)

        // when
        val result = TreeNode.findOrCreateChild(parent, "child3")

        // then
        assertEquals("child3", result.name)
        assertEquals(3, parent.children.size)
        assertEquals(result, parent.children[2])
    }

    @Test
    fun should_printPackageTree() {
        // given
        val root = TreeNode("root")
        val orgNode = TreeNode("org")
        val archguardNode = TreeNode("archguard")
        val architectureNode = TreeNode("architecture")
        val backendNode = TreeNode("backend")

        root.children.add(orgNode)
        orgNode.children.add(archguardNode)
        archguardNode.children.add(architectureNode)
        archguardNode.children.add(backendNode)

        // when
        val output = captureOutput { TreeNode.printPackageTree(root) }

        // then
        val expectedOutput = """
        root
          org
            archguard
              architecture
              backend
    """.trimIndent().replace("\n", System.lineSeparator())

        assertEquals(expectedOutput, output)
    }

    private fun captureOutput(block: () -> Unit): String {
        val outputStream = ByteArrayOutputStream()
        val printStream = PrintStream(outputStream)
        val originalOut = System.out

        System.setOut(printStream)

        block()

        System.out.flush()
        System.setOut(originalOut)

        return outputStream.toString().trim()
    }
}

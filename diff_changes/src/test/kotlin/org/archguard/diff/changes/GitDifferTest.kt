package org.archguard.diff.changes

import org.eclipse.jgit.api.Git
import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertEquals

internal class GitDifferTest {

    @Test
    fun should_get_range_for_local_java() {
        val localPath = File("./build/ddd")

        // if test in local, skip clone
        if (!File(localPath, ".git").isDirectory) {
            Git.cloneRepository()
                .setURI("https://github.com/archguard/ddd-monolithic-code-sample")
                .setDirectory(localPath)
                .call()
        }

        val differ = GitDiffer("./build/ddd", "master", 7)
        val calculateChange = differ.countBetween("5952edc", "f3fb4e2")

        assertEquals(3, calculateChange.size)

        val relations = calculateChange[0].relations
        assertEquals(1, relations.size)
        assertEquals("com.dmall.productservice.apis.ProductController.getAllProducts", relations.last().source)
        assertEquals("com.dmall.productservice.apis.assembler.ProductAssembler.toProductResponseList", relations.last().target)
    }
}

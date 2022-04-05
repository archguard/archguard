package org.archguard.diff.changes

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class GitDifferTest {

    @Test
    fun should_get_range_for_local() {
        val differ = GitDiffer("..", "master", "", "")
        val diffList = differ
            .countInRange("97d1ff56", "80fb1245")

        val functions = diffList.functions

        assertEquals(1, functions.size)

        assertEquals("saveClassFields", functions[0].functionName)

        differ.generateFunctionCallMap()
        differ.generateProjectFunctionMap()

        differ.calculateChange()
    }
}
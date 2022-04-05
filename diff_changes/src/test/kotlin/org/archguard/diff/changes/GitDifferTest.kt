package org.archguard.diff.changes

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class GitDifferTest {

    @Test
    fun should_get_range_for_local() {
        val diffList = GitDiffer("..", "master", "", "")
            .countInRange("97d1ff56", "80fb1245")

        val functions = diffList.functions

        assertEquals(1, functions.size)

        assertEquals("saveClassFields", functions[0].functionName)
    }
}
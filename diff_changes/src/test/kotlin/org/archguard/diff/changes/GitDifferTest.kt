package org.archguard.diff.changes

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class GitDifferTest {

    @Test
    fun should_get_range_for_local() {
        val differ = GitDiffer("..", "master", "", "")
        val diffList = differ
            .countInRange("2ed0b8eb", "5fd08b56")

        val functions = diffList.functions

        assertEquals(1, functions.size)

        assertEquals("getKey", functions[0].functionName)

        differ.generateProjectFunctionMap()
        differ.generateFunctionCallMap()

        differ.calculateChange()
    }
}
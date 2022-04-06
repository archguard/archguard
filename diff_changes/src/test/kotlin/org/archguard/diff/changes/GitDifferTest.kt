package org.archguard.diff.changes

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class GitDifferTest {

    @Test
    fun should_get_range_for_local() {
        val differ = GitDiffer("..", "master", "", "")
        val diffList = differ
            .countInRange("aa2b5379", "965be8c2")

        val functions = diffList.functions

        assertEquals(1, functions.size)
        assertEquals("generateBatchInsertSql", functions[0].functionName)

        differ.genFunctionMap()
        differ.genFunctionCallMap()

        differ.calculateChange()
    }
}
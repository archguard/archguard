package org.archguard.diff.changes

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class GitDifferTest {

    @Test
    fun should_get_range_for_local() {
        val ds = GitDiffer("..", "master", "", "")
            .countInRange("97d1ff56", "80fb1245")

        assertEquals(2, ds.size)

        val codeFunction = ds[1].Functions.filter { it.Name == "saveClassFields" }[0]
        val printlnCalls = codeFunction.FunctionCalls.filter { it.FunctionName == "println" }
        assertEquals(2, printlnCalls.size)
    }
}
package org.archguard.diff.changes

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class GitDifferTest {

    @Test
    fun should_get_range_for_local() {
        val differ = GitDiffer("..", "master", "")
        val calculateChange = differ.countInRange("aa2b5379", "965be8c2")

        assertEquals(1, calculateChange.size)

        val relations = calculateChange[0].relations
        assertEquals(6, relations.size)
        assertEquals("infrastructure.SourceBatch.execute", relations.last().source)
        assertEquals("infrastructure.utils.SqlGenerator.generateBatchInsertSql", relations.last().target)
    }
}
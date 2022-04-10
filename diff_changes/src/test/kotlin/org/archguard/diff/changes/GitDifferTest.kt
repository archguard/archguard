package org.archguard.diff.changes

import org.junit.Ignore
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class GitDifferTest {

    @Test
    @Ignore
    fun should_get_range_for_local_java() {
        val differ = GitDiffer("..", "master")
        val calculateChange = differ.countBetween("aa2b5379", "965be8c2")

        assertEquals(1, calculateChange.size)

        val relations = calculateChange[0].relations
        assertEquals(6, relations.size)
        assertEquals("infrastructure.SourceBatch.execute", relations.last().source)
        assertEquals("infrastructure.utils.SqlGenerator.generateBatchInsertSql", relations.last().target)
    }

    @Test
    @Ignore
    fun should_get_range_for_local_kotlin() {
        val differ = GitDiffer("..", "master")
        val calculateChange = differ.countBetween("92f1f59f", "d31422bd")

        assertEquals(1, calculateChange.size)

        val relations = calculateChange[0].relations
        assertEquals(0, relations.size)
//        assertEquals("infrastructure.SourceBatch.execute", relations.last().source)
//        assertEquals("infrastructure.utils.SqlGenerator.generateBatchInsertSql", relations.last().target)
    }
}
package org.archguard.diff.changes

import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class GitDifferTest {

    @Test
    @Disabled("current only working with Java")
    fun should_get_range_for_local() {
        val differ = GitDiffer("..", "master", "", "")
        val diffList = differ
            .countInRange("aa2b5379", "965be8c2")

        val functions = diffList.functions

        assertEquals(1, functions.size)
        assertEquals("generateBatchInsertSql", functions[0].functionName)

        differ.genFunctionMap()
        differ.genFunctionCallMap()

        assertEquals("org.archguard.scanner.common.ClassRepository.flush -> infrastructure.SourceBatch.execute;\n" +
                "org.archguard.scanner.common.ClassRepository.close -> infrastructure.SourceBatch.execute;\n" +
                "org.archguard.scanner.common.ContainerRepository.close -> infrastructure.SourceBatch.execute;\n" +
                "org.archguard.scanner.common.DatamapRepository.flush -> infrastructure.SourceBatch.execute;\n" +
                "org.archguard.scanner.common.DatamapRepository.close -> infrastructure.SourceBatch.execute;\n" +
                "infrastructure.SourceBatch.execute -> infrastructure.utils.SqlGenerator.generateBatchInsertSql;\n", differ.calculateChange().joinToString("\n"))
    }
}
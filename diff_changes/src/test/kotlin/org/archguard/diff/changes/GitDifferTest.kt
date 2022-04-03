package org.archguard.diff.changes

import org.junit.jupiter.api.Test

internal class GitDifferTest {

    @Test
    fun should_get_range_for_local() {
        GitDiffer.getRange("..", "master", "97d1ff56", "80fb1245")
    }
}
package org.archguard.diff.changes

import org.junit.jupiter.api.Test

internal class GitDifferTest {

    @Test
    fun should_get_range_for_local() {
        GitDiffer("..", "master", "", "")
            .countInRange("97d1ff56", "80fb1245")
    }
}
package org.archguard.domain.insight

import org.junit.jupiter.api.Assertions.*

internal class InsightFieldFilterTest {

    @org.junit.jupiter.api.Test
    fun normal_validate() {
        assert(InsightFieldFilter(InsightFilterType.NORMAL, "log4j").validate("log4j"))
        assertFalse(InsightFieldFilter(InsightFilterType.NORMAL, "log4j").validate("log4j:log4j"))
    }

    @org.junit.jupiter.api.Test
    fun validate_regex() {
        assert(InsightFieldFilter(InsightFilterType.REGEXP, "/.*log4j/").validate("log4j:log4j"))
    }
}
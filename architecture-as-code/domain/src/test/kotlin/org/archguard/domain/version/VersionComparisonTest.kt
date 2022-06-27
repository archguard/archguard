package org.archguard.domain.version

import org.junit.jupiter.api.Test

internal class VersionComparisonTest {
    @Test
    fun filter_fit_type() {
        val left = VersionNumber.parse("1.2.3")!!

        assert(VersionComparison(left, "==").toOther("1.2.3"))
        assert(VersionComparison(left, "<").toOther("1.2.4"))
        assert(VersionComparison(left, ">=").toOther("1.2.3"))
        assert(VersionComparison(left, ">=").toOther("1.2.2"))

        assert(VersionComparison(VersionNumber.parse("1.2.3-beta")!!, ">=").toOther("1.2.3-alpha"))
    }

    @Test
    fun compare_to_other() {
        assert(VersionComparison().eval("1.2.3", "==", "1.2.3"))
        assert(VersionComparison().eval("1.2.2", "<=", "1.2.3"))
    }

    @Test
    fun not_equal() {
        assert(VersionComparison().eval("1.2.3", "!=", "1.2.2"))
        assert(VersionComparison().eval("1.2.2", "!=", "1.2.4"))
    }
}
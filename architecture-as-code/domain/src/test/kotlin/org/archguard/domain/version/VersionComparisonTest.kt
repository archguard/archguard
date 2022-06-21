package org.archguard.domain.version

import org.junit.jupiter.api.Test

internal class VersionComparisonTest {
    @Test
    fun filter_fit_type() {
        val left = VersionNumber.parse("1.2.3")!!

        assert(VersionComparison(left, "==").isFit("1.2.3"))
        assert(VersionComparison(left, "<").isFit("1.2.4"))
        assert(VersionComparison(left, ">=").isFit("1.2.3"))
        assert(VersionComparison(left, ">=").isFit("1.2.2"))

        assert(VersionComparison(VersionNumber.parse("1.2.3-beta")!!, ">=").isFit("1.2.3-alpha"))
    }
}
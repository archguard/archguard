package org.archguard.scanner.ctl.impl

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class OfficialAnalyserSpecsTest {
    @Test
    fun `should output the spec with the default jar for all the official analysers`() {
        val specs = OfficialAnalyserSpecs.specs()

        assertThat(specs).allMatch {
            it.jar == "${it.identifier}-${it.version}-all.jar"
        }
    }
}

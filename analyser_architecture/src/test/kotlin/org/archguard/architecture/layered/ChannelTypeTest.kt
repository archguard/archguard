package org.archguard.architecture.layered

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class ChannelTypeTest {
    @Test
    fun test_all_values() {
        ChannelType.values().size shouldBe 16
        ChannelType.values().first().displayName shouldBe "Website"
    }

}
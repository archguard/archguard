package com.thoughtworks.archguard.scanner2.domain.service

import com.thoughtworks.archguard.scanner2.domain.repository.JClassRepository
import com.thoughtworks.archguard.scanner2.domain.repository.JMethodRepository
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import org.archguard.model.Dependency
import org.archguard.model.FanInFanOut
import org.archguard.operator.FanInFanOut.calculateFanInFanOutWithDependency
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class FanInFanOutServiceTest {
    private lateinit var fanInFanOutService: FanInFanOutService

    @MockK
    private lateinit var jClassRepository: JClassRepository

    @MockK
    private lateinit var jMethodRepository: JMethodRepository

    @BeforeEach
    internal fun setUp() {
        MockKAnnotations.init(this)
        fanInFanOutService = FanInFanOutService(jClassRepository, jMethodRepository)
    }

    @Test
    internal fun should_calculate_class_level_fanin_fanout() {
        val d1 = Dependency("c1", "c2")
        val d2 = Dependency("c1", "c3")
        val d3 = Dependency("c3", "c1")
        val d4 = Dependency("c1", "c5")
        val allClassDependencies = listOf(d1, d2, d3, d4)

        val fanInFanOutMap = calculateFanInFanOutWithDependency(allClassDependencies)
        assertThat(fanInFanOutMap).containsExactlyInAnyOrderEntriesOf(
            mapOf(
                "c1" to FanInFanOut(1, 3), "c2" to FanInFanOut(1, 0),
                "c3" to FanInFanOut(1, 1), "c5" to FanInFanOut(1, 0)
            )
        )
    }
}

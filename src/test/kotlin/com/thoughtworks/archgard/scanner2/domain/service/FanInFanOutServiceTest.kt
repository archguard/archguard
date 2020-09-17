package com.thoughtworks.archgard.scanner2.domain.service

import com.thoughtworks.archgard.scanner2.domain.model.Dependency
import com.thoughtworks.archgard.scanner2.domain.repository.JClassRepository
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class FanInFanOutServiceTest {
    private lateinit var fanInFanOutService: FanInFanOutService

    @MockK
    private lateinit var jClassRepository: JClassRepository

    @BeforeEach
    internal fun setUp() {
        MockKAnnotations.init(this)
        fanInFanOutService = FanInFanOutService(jClassRepository)
    }

    @Test
    internal fun should_calculate_class_level_fanin_fanout() {
        val d1 = Dependency("c1", "c2")
        val d2 = Dependency("c1", "c3")
        val d3 = Dependency("c3", "c1")
        val d4 = Dependency("c1", "c5")
        val allClassDependencies = listOf(d1, d2, d3, d4)

        val jClassIdNotThirdParty = listOf("c1", "c2", "c3")
        val fanInFanOutMap = fanInFanOutService.calculateClassFanInFanOutWithClassDependency(allClassDependencies, jClassIdNotThirdParty)
        assertThat(fanInFanOutMap).containsExactlyInAnyOrderEntriesOf(mapOf("c1" to FanInFanOut(1, 3), "c2" to FanInFanOut(1, 0), "c3" to FanInFanOut(1, 1)))

    }
}
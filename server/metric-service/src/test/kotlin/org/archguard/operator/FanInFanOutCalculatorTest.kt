package org.archguard.operator;

import org.archguard.model.Dependency
import org.archguard.model.FanInFanOut
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class FanInFanOutCalculatorTest {

    @Test
    fun shouldCalculateFanInFanOutWithDependency() {
        // Given
        val dependencies = listOf(
            Dependency("ClassA", "ClassB"),
            Dependency("ClassA", "ClassC"),
            Dependency("ClassB", "ClassC"),
            Dependency("ClassD", "ClassA")
        )

        // When
        val result = calculateFanInFanOutWithDependency(dependencies)

        // Then
        assertEquals(4, result.size)
    }

    @Test
    internal fun should_calculate_class_level_fanin_fanout() {
        val d1 = Dependency("c1", "c2")
        val d2 = Dependency("c1", "c3")
        val d3 = Dependency("c3", "c1")
        val d4 = Dependency("c1", "c5")
        val allClassDependencies = listOf(d1, d2, d3, d4)

        val fanInFanOutMap = calculateFanInFanOutWithDependency(allClassDependencies)
        Assertions.assertThat(fanInFanOutMap).containsExactlyInAnyOrderEntriesOf(
            mapOf(
                "c1" to FanInFanOut(1, 3), "c2" to FanInFanOut(1, 0),
                "c3" to FanInFanOut(1, 1), "c5" to FanInFanOut(1, 0)
            )
        )
    }
}

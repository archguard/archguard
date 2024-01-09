package org.archguard.metric.coupling;

import org.archguard.arch.LogicModule
import org.archguard.model.vos.JClassVO
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ModuleCouplingTest {

    @Test
    fun should_createModuleCoupling_withCorrectValues() {
        // Given
        val logicModule = LogicModule.create("", "", listOf(), listOf())
        val classVo = JClassVO("HelloWorld", "")
        val classCouplings = listOf(
            ClassCoupling(classVo, 5, 6, 7, 8),
            ClassCoupling(classVo, 3, 4, 5, 6),
            ClassCoupling(classVo, 2, 3, 4, 5)
        )

        // When
        val moduleCoupling = ModuleCoupling.of(logicModule, classCouplings)

        // Then
        assertEquals(logicModule, moduleCoupling.logicModule)
        assertThat(moduleCoupling.outerInstabilityAvg > 0.54)
    }
}
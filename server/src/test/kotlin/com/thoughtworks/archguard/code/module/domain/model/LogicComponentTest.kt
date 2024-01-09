package com.thoughtworks.archguard.code.module.domain.model

import org.archguard.arch.LogicModule
import org.archguard.model.vos.JClassVO
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class LogicComponentTest {
    @Test
    internal fun should_contain_logic_module_when_in_members() {
        val module1 = SubModule("Module1")
        val jClass2 = JClassVO.create("Module2.Jclass2")
        val jClass3 = JClassVO.create("Module2.Jclass3")
        val module3 = SubModule("Module3")
        val module4 = SubModule("Module4")
        val lg1 = LogicModule("id2", "lg2", listOf(module4, jClass3))
        val lg2 = LogicModule("id2", "lg2", listOf(module3, lg1))
        val logicModule = LogicModule(
            "id10", "lg",
            listOf(module1, jClass2, lg2)
        )

        assertThat(logicModule.containsOrEquals(module1)).isTrue()
        assertThat(logicModule.containsOrEquals(jClass2)).isTrue()
        assertThat(logicModule.containsOrEquals(lg2)).isTrue()
        assertThat(logicModule.containsOrEquals(lg1)).isTrue()
        assertThat(logicModule.containsOrEquals(module3)).isTrue()
        assertThat(logicModule.containsOrEquals(module4)).isTrue()
        assertThat(logicModule.containsOrEquals(jClass3)).isTrue()
    }
}

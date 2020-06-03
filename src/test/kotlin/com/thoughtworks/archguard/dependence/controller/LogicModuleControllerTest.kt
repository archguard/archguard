package com.thoughtworks.archguard.dependence.controller

import com.thoughtworks.archguard.dependence.domain.logic_module.LogicModuleService
import com.thoughtworks.archguard.dependence.domain.logic_module.ModuleCoupling
import io.mockk.MockKAnnotations.init
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class LogicModuleControllerTest {
    @MockK
    lateinit var service: LogicModuleService
    @InjectMockKs
    var controller = LogicModuleController()

    @BeforeEach
    fun setUp() {
        init(this)
    }

    @Test
    fun `should get module coupling`() {
        //given
        val moduleCoupling = ModuleCoupling()
        every { service.getLogicModuleCoupling() } returns moduleCoupling

        //when
        val logicModuleCoupling = controller.getLogicModuleCoupling()
        //then
        assertThat(logicModuleCoupling).isEqualToComparingFieldByField(moduleCoupling)
    }
}
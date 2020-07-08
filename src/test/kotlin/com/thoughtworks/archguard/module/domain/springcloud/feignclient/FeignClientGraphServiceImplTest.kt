package com.thoughtworks.archguard.module.domain.springcloud.feignclient

import com.thoughtworks.archguard.module.domain.JClassRepository
import com.thoughtworks.archguard.module.domain.LogicModuleRepository
import com.thoughtworks.archguard.module.domain.model.Dependency
import com.thoughtworks.archguard.module.domain.model.JClass
import com.thoughtworks.archguard.module.domain.model.LogicModule
import com.thoughtworks.archguard.module.domain.model.ModuleMember
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class FeignClientGraphServiceImplTest {
    @MockK
    lateinit var logicModuleRepository: LogicModuleRepository

    @MockK
    lateinit var jClassRepository: JClassRepository

    @MockK
    lateinit var feignClientService: FeignClientService

    private lateinit var service: FeignClientGraphServiceImpl

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        val feignClient = FeignClient(JClass("id1remote", "class1remote", "module1"), FeignClientArg("module2"))
        every { feignClientService.getFeignClients() } returns listOf(feignClient)

        service = FeignClientGraphServiceImpl(logicModuleRepository, jClassRepository, feignClientService)
    }

    @Test
    internal fun should_find_real_module_dependency() {

        // given
        val class1 = JClass("id1", "class1", "module1")
        val class1remote = JClass("id1remote", "class1remote", "module1")
        val class2 = JClass("id2", "class2", "module2")
        val logicModule1 = LogicModule("m1", "logicModule1", listOf(ModuleMember.create("module1")))
        val logicModule2 = LogicModule("m2", "logicModule2", listOf(ModuleMember.create("module2")))
        val dependency = Dependency(class1, class1remote)


        // when
        val dependencies = service.mapClassDependenciesToModuleDependencies(listOf(dependency), listOf(logicModule1, logicModule2))

        // then
        assertThat(dependencies).isEqualTo(listOf(Dependency(logicModule1, logicModule2)))

    }
}

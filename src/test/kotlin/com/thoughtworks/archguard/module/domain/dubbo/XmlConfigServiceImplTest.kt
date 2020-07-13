package com.thoughtworks.archguard.module.domain.dubbo

import com.thoughtworks.archguard.module.domain.model.JClass
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class XmlConfigServiceImplTest {
    @MockK
    lateinit var dubboConfigRepository: DubboConfigRepository

    @InjectMockKs
    var service: XmlConfigService = XmlConfigServiceImpl()

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    internal fun should_find_real_sub_module() {
        val callerClass = JClass("id1", "caller1", "module1")
        val calleeClass = JClass("id2", "callee1", "module2")
        val callerSubModule = SubModuleDubbo("id_module1", "module1", "module_path1")
        val calleeImplSubModule = SubModuleDubbo("id_module2", "module2", "module_path2")
        val referenceConfig = ReferenceConfig("reference_id1", "bean_id1", "callee1", null, "g1", callerSubModule)
        val serviceConfig = ServiceConfig("service_id1", "callee1", "callee1A", null, "g1", calleeImplSubModule)

        every { dubboConfigRepository.getModuleByName("module1") } returns callerSubModule
        every { dubboConfigRepository.getReferenceConfigBy("callee1", callerSubModule) } returns listOf(referenceConfig)
        every { dubboConfigRepository.getServiceConfigBy(referenceConfig) } returns listOf(serviceConfig)

        val module = service.getRealCalleeModuleByXmlConfig(callerClass, calleeClass)

        assertThat(module).isEqualTo(listOf(calleeImplSubModule))
    }
}
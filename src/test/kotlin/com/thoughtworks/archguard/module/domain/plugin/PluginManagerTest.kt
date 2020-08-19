package com.thoughtworks.archguard.module.domain.plugin

import com.thoughtworks.archguard.config.domain.Configure
import com.thoughtworks.archguard.config.domain.ConfigureRepository
import com.thoughtworks.archguard.module.domain.dubbo.DubboPlugin
import com.thoughtworks.archguard.module.domain.springcloud.feignclient.FeignClientPlugin
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationContext
import kotlin.test.assertEquals

class PluginManagerTest {
    @MockK
    lateinit var configRepository: ConfigureRepository

    @MockK
    lateinit var applicationContext: ApplicationContext

    @MockK
    lateinit var dubboPlugin: DubboPlugin
    @MockK
    lateinit var feignClientPlugin: FeignClientPlugin

    private lateinit var pluginManager: PluginManager

    private val log = LoggerFactory.getLogger(PluginManagerTest::class.java)

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)
        pluginManager = PluginManager(configRepository)
    }


    @Test
    fun should_get_FeignClients() {

        // given
        val config1 = Configure("id1", 1L, "plugin", "name", PluginType.DUBBO.name, 1)
        val config2 = Configure("id1", 1L, "plugin", "name", PluginType.FEIGN_CLIENT.name, 2)


        every { configRepository.getConfigures(1L) } returns listOf(config1, config2)
        every { applicationContext.getBeansOfType(DependPlugin::class.java) } returns mapOf(PluginType.DUBBO.name to dubboPlugin, PluginType.FEIGN_CLIENT.name to feignClientPlugin)
        every { dubboPlugin.getPluginType() } returns PluginType.DUBBO
        every { feignClientPlugin.getPluginType() } returns PluginType.FEIGN_CLIENT

        pluginManager.setApplicationContext(applicationContext)
        // when
        val plugins = pluginManager.getDependPlugin<DependPlugin>(1L)

        // then
        assertEquals(2, plugins.size)
        assertEquals(dubboPlugin, plugins[0])
        assertEquals(feignClientPlugin, plugins[1])

    }
}

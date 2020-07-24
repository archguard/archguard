package com.thoughtworks.archguard.module.domain.plugin

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
    lateinit var pluginConfigRepository: PluginConfigRepository

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
        pluginManager = PluginManager(pluginConfigRepository, applicationContext)
    }


    @Test
    fun should_get_FeignClients() {

        // given
        val pluginConfig = listOf(PluginConfig("DubboPlugin"), PluginConfig(("FeiginPlugin")))

        every { pluginConfigRepository.getAll() } returns pluginConfig
        every { applicationContext.getBeansOfType(Plugin::class.javaObjectType) } returns mapOf("DubboPlugin" to dubboPlugin, "FeiginPlugin" to feignClientPlugin)

        // when
        val plugins = pluginManager.getPlugins()

        // then
        assertEquals(2, plugins.size)
        assertEquals(dubboPlugin, plugins[0])
        assertEquals(feignClientPlugin, plugins[1])

    }
}

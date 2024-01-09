package com.thoughtworks.archguard.method.domain.service

import com.thoughtworks.archguard.code.method.domain.JMethod
import com.thoughtworks.archguard.code.method.domain.service.MethodConfigService
import org.archguard.config.Configure
import com.thoughtworks.archguard.config.domain.ConfigureRepository
import com.thoughtworks.archguard.config.domain.ConfigureService
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class MethodConfigServiceTest {
    @MockK
    private lateinit var configureRepository: ConfigureRepository
    private lateinit var configureService: ConfigureService
    private lateinit var methodConfigService: MethodConfigService

    @BeforeEach
    internal fun setUp() {
        MockKAnnotations.init(this)
        configureService = ConfigureService(configureRepository)
        methodConfigService = MethodConfigService(configureService)
    }

    @Test
    internal fun should_add_color_config_info_for_every_method() {
        val jMethod1 = JMethod("id1", "print", "com.Exception", "m1", "void", emptyList())
        val jMethod2 = JMethod("id2", "isRight", "org.Answer", "m1", "boolean", emptyList())
        val jMethods: List<JMethod> = mutableListOf(jMethod1, jMethod2)

        val systemId = 1L
        val configure1 = Configure("cid1", 1L, "nodeColor", "print", "#ff8975", 1)
        val configure2 = Configure("cid2", 1L, "nodeColor", "print", "#ff8999", 2)
        val configure3 = Configure("cid3", 1L, "nodeDisplay", "logger", "hidden", 2)
        val configs: List<Configure> = mutableListOf(configure1, configure2, configure3)
        every { configureRepository.getConfigures(systemId) } returns configs

        methodConfigService.buildColorConfig(jMethods, systemId)
        assertThat(jMethods[0].configuresMap["nodeColor"]).isEqualTo("#ff8999")
        assertThat(jMethods[1].configuresMap["nodeColor"]).isNull()
    }
}

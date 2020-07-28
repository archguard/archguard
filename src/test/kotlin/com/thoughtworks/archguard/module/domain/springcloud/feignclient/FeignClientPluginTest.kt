package com.thoughtworks.archguard.module.domain.springcloud.feignclient

import com.thoughtworks.archguard.module.domain.model.Dependency
import com.thoughtworks.archguard.module.domain.model.JMethodVO
import com.thoughtworks.archguard.module.domain.springcloud.SpringCloudServiceRepository
import com.thoughtworks.archguard.module.domain.springcloud.httprequest.HttpRequest
import com.thoughtworks.archguard.module.domain.springcloud.httprequest.HttpRequestArg
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class FeignClientPluginTest {
    @MockK
    lateinit var feignClientService: FeignClientService

    @MockK
    lateinit var springCloudServiceRepository: SpringCloudServiceRepository

    @InjectMockKs
    lateinit var plugin: FeignClientPlugin

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun should_fix_method_dependencies() {
        // given
        val method1 = JMethodVO( "method1", "class1", "module1")
        val method2 = JMethodVO("method2", "class2", "module2")
        val method3 = JMethodVO("method3", "class3", "module3")

        val methodDependencies = listOf(Dependency(method1, method2))

        every { feignClientService.getFeignClientMethodDependencies() } returns listOf(Dependency(HttpRequest("id2", HttpRequestArg()), HttpRequest("id3", HttpRequestArg())))
        every { springCloudServiceRepository.getMethodById("id2") } returns method2
        every { springCloudServiceRepository.getMethodById("id3") } returns method3

        // when
        val methodMethodDependenciesAfterFix = plugin.fixMethodDependencies(methodDependencies)

        // then
        Assertions.assertThat(methodMethodDependenciesAfterFix).isEqualTo(methodDependencies + Dependency(method2, method3))

    }

}

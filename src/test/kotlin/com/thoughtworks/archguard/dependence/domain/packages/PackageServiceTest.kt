package com.thoughtworks.archguard.dependence.domain.packages

import com.thoughtworks.archguard.dependence.domain.module.BaseModuleRepository
import com.thoughtworks.archguard.dependence.infrastructure.packages.PackageDependenceDTO
import io.mockk.MockKAnnotations.init
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class PackageServiceTest {
    @MockK
    lateinit var packageRepository:PackageRepository
    @MockK
    lateinit var moduleRepository: BaseModuleRepository
    @InjectMockKs
    var packageService = PackageService()

    @BeforeEach
    fun setUp() {
        init(this)
    }

    @Test
    internal fun `should get package dependencies by modules`() {
        //given
        every { moduleRepository.getBaseModules() } returns listOf("com.module1", "com.module2")
        val dependencies1 = listOf<PackageDependenceDTO>(PackageDependenceDTO("org.wrapper.ThrowablePB\$ThrowableProto",
                "org.test.ThrowablePB\$ThrowableProto"))
        val dependencies2 = listOf<PackageDependenceDTO>()
        every { packageRepository.getPackageDependenceByModule("com.module1") } returns dependencies1
        every { packageRepository.getPackageDependenceByModule("com.module2") } returns dependencies2
        //when
        val packageDependencies = packageService.getPackageDependencies()
        //then
        assertThat(packageDependencies.size).isEqualTo(2)
        assertThat(packageDependencies[0].packageGraph.nodes.size).isEqualTo(3)
        assertThat(packageDependencies[0].packageGraph.edges.size).isEqualTo(1)
        assertThat(packageDependencies[1].packageGraph.nodes.size).isEqualTo(0)
        assertThat(packageDependencies[1].packageGraph.edges.size).isEqualTo(0)
    }
}
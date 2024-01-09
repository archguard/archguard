package com.thoughtworks.archguard.packages.domain

import com.thoughtworks.archguard.code.module.domain.LogicModuleRepository
import org.archguard.arch.SubModule
import com.thoughtworks.archguard.code.packages.domain.PackageRepository
import com.thoughtworks.archguard.code.packages.domain.PackageService
import com.thoughtworks.archguard.code.packages.infrastructure.PackageDependenceDTO
import io.mockk.MockKAnnotations.init
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class PackageServiceTest {
    @MockK
    lateinit var packageRepository: PackageRepository

    @MockK
    lateinit var moduleRepository: LogicModuleRepository

    @InjectMockKs
    var packageService = PackageService()

    @BeforeEach
    fun setUp() {
        init(this)
    }

    @Test
    internal fun `should get package dependencies by modules`() {
        // given
        val systemId: Long = 1
        every { moduleRepository.getAllSubModule(systemId) } returns listOf(SubModule("com.module1"), SubModule("com.module2"))
        val dependencies1 = listOf(
            PackageDependenceDTO(
                "org.wrapper.ThrowablePB\$ThrowableProto",
                "org.test.ThrowablePB\$ThrowableProto"
            )
        )
        val dependencies2 = listOf<PackageDependenceDTO>()
        every { packageRepository.getPackageDependenceByModuleFull(systemId, "com.module1") } returns dependencies1
        every { packageRepository.getPackageDependenceByModuleFull(systemId, "com.module2") } returns dependencies2
        // when
        val packageDependencies = packageService.getPackageDependencies(systemId, "jvm")
        // then
        assertThat(packageDependencies.size).isEqualTo(2)
        assertThat(packageDependencies[0].packageGraph.nodes.size).isEqualTo(3)
        assertThat(packageDependencies[0].packageGraph.edges.size).isEqualTo(1)
        assertThat(packageDependencies[1].packageGraph.nodes.size).isEqualTo(0)
        assertThat(packageDependencies[1].packageGraph.edges.size).isEqualTo(0)
    }
}
